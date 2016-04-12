package com.x.yang.thingstodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimerTask;

/**
 * Created by yang on 2016/4/11.
 */
public class backService extends Service {

    private Location location1;
    public ArrayList<Thing_data> list,list_today;
    public ArrayList<String>list_near;
    private SQLiteDatabase db;
    private FileIssue fi;
    NewDReceiver receiver;
    String idofnearest,idofnext;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("serviceYX","begin");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("serviceYX","start");
        list = new ArrayList<Thing_data>();
        list_today = new ArrayList<Thing_data>();
        list_near = new ArrayList<String>();
        fi = new FileIssue(this);
        db = fi.getReadableDatabase();
        Log.i("service","locationThread");

        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        location1=locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 1000*60, 100, locationListener);
        new readingData().execute();
        receiver=new NewDReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.NEWEVENT_S");
        this.registerReceiver(receiver, filter);
        return Service.START_STICKY;
    }
    public class NewDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle=intent.getExtras();
            String v;
            new readingData().execute();
        }
    }
    private class readingData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("service","readingThread");
            Cursor c = db.rawQuery("SELECT * FROM things", null);
            Calendar calendar = Calendar.getInstance();

            while (c.moveToNext()) {
                Thing_data thing = new Thing_data();
                thing.setId(c.getString(c.getColumnIndex("id")));
                thing.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
                thing.setLoc(c.getString(c.getColumnIndex("location")));
                thing.setLongtitude(c.getDouble(c.getColumnIndex("longtitude")));
                thing.setMess(c.getString(c.getColumnIndex("messageadte")));
                thing.setDay(c.getInt(c.getColumnIndex("day")));
                thing.setHour(c.getInt(c.getColumnIndex("hour")));
                thing.setYear(c.getInt(c.getColumnIndex("year")));
                thing.setMonth(c.getInt(c.getColumnIndex("month")));
                thing.setMin(c.getInt(c.getColumnIndex("min")));
                thing.setFre(c.getString(c.getColumnIndex("repeat")));
                thing.setTitle(c.getString(c.getColumnIndex("title")));

                if(thing.getDay() == calendar.get(Calendar.DAY_OF_MONTH)&&thing.getMonth() == calendar.get(Calendar.MONTH)&& thing.getYear() == calendar.get(Calendar.YEAR)){
                    list_today.add(thing);
                }else if(thing.getFre() == "daily"){
                    list_today.add(thing);
                }else if(thing.getFre() == "monthly" && thing.getDay() == calendar.get(Calendar.DAY_OF_MONTH)){
                    list_today.add(thing);
                }
                list.add(thing);
            }
            c.close();
            Log.i("mess","i am here2");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            new compareTask().execute();

        }
    }
    class compareTask extends AsyncTask{
        int nexthour=24;
        int nextmin=60;
        float mindis = 20000.0F;
        String id_o_n = "no things to do today";
        String id_o_near = "no things less than 2KM";

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("service","compareThread");
            Iterator <Thing_data>it = list_today.iterator();
            Iterator <Thing_data>it2 = list.iterator();
            while(it.hasNext()){
                Thing_data td =it.next();
                AlarmManager alarmManager=null;
                int hour,min,i;
                hour = td.getHour();
                min =td.getMin();
                if(hour < nexthour || (hour == nexthour && min < nextmin)){
                    id_o_n = td.getId();
                }
                Calendar c=Calendar.getInstance();
                i = c.get(Calendar.SECOND)+c.get(Calendar.MINUTE);
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR, hour);
                c.set(Calendar.MINUTE, min);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                Log.i("today evet","added one");
                Intent intent = new Intent(backService.this, MainPage.class);
                intent.setAction(it.next().getId());
                intent.putExtra("id",it.next().getId());
                PendingIntent pi = PendingIntent.getBroadcast(backService.this, 0, intent, 0);    //创建PendingIntent
                //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
            }
            list_near.clear();
            while(it2.hasNext()){
                float[] results;
                results = new float[1];
                Location.distanceBetween(location1.getLatitude(), location1.getLongitude(), it2.next().getLatitude(), it2.next().getLongtitude(), results);

                if(results[0] <1000){
                    list_near.add(it2.next().getId());
                    if(results[0] < mindis){
                        id_o_near = it2.next().getId();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            idofnext = id_o_n;
            idofnearest = id_o_near;
        }
    }
    class TimerTasks extends AsyncTask {
        TimerTask task;

        @Override
        protected Object doInBackground(Object[] params) {


            Log.i("cur", "12333");


            return null;
        }
    }
    private final LocationListener locationListener = new LocationListener()
    {
        public void onLocationChanged(Location location) {
            location1 = location;
            Intent intnet = new Intent("com.x.yang.thingstodo.GPSREADY");
            Log.i("GPS","update new location");
            sendBroadcast(intnet);
            new compareTask().execute();

        }
        public void onProviderDisabled(String provider) {
            //Toast.makeText(MainPage.this,"please open your locate function",Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String provider) {
           // Toast.makeText(MainPage.this,"locate function enabled",Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

            if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE){

            }
        }
    };
}
