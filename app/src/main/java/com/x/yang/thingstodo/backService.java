package com.x.yang.thingstodo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
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
import java.util.Timer;
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
    int has = 0;
    Timer timer;
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
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        location1=locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 1000*180, 2000, locationListener);
        new readingData().execute();
        receiver=new NewDReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.NEWEVENT_S");
        this.registerReceiver(receiver, filter);
        timer = new Timer(true);
        timer.schedule(new compTask(),3600000,3600000);
        return Service.START_STICKY;
    }
    private class compTask extends TimerTask{
        @Override
        public void run() {
            new compareTask().execute();
        }
    }
    public class NewDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle=intent.getExtras();
            String v;
            Log.i("service","receivedboardcast");
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
        int nexthour=25;
        int nextmin=61;
        float mindis = 20000.0F;
        String id_o_n = "nu";
        String id_o_near = "nu";

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("service","compareThread");
            int hs=0;
            AlarmManager alarmManager=(AlarmManager)backService.this.getSystemService(Context.ALARM_SERVICE);
            Iterator <Thing_data>it = list_today.iterator();
            Iterator <Thing_data>it2 = list.iterator();
            Calendar c = Calendar.getInstance();
            while(it.hasNext()){
                Thing_data td =it.next();

                int hour,min,i;
                hour = td.getHour();
                min =td.getMin();
                if((c.get(Calendar.HOUR_OF_DAY)+2>hour && (c.get(Calendar.HOUR_OF_DAY)+1)<=hour) || (c.get(Calendar.MINUTE)<min && c.get(Calendar.HOUR_OF_DAY)==hour)) {
                    id_o_n = td.getId();
                    hs++;
                }
                /*
                Calendar c2=Calendar.getInstance();
                i = c.get(Calendar.SECOND)+c.get(Calendar.MINUTE);




                int t =c.get(Calendar.HOUR_OF_DAY)*60*60*1000+c.get(Calendar.MINUTE)*60*1000;
                int s=(min)*60*1000+hour*60*60*1000;
                if(s-t>0){
                    c2.set(Calendar.HOUR_OF_DAY,hour);
                    c2.set(Calendar.MINUTE,min);

                    if(s>0){
                        Log.i("service","added one alarm");
                        Log.i("service","s+t"+(s-t));
                    Intent intent = new Intent(backService.this, MainPage.class);

                    intent.putExtra("id",td.getId());
                    intent.putExtra("action","timeup");
                    PendingIntent pi = PendingIntent.getActivity(backService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, c2.getTimeInMillis(), pi);*/
                    }
            if(hs != 0 && hs>has){
                has = hs;
                NotificationManager manger = (NotificationManager) backService.this.getSystemService(Context.NOTIFICATION_SERVICE);


                Notification notification = new Notification();
                //notification.icon = R.drawable.notice;
                Intent intent = new Intent(backService.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("action","nearevent");
                intent.putStringArrayListExtra("list_near",list_near);
                PendingIntent pi = PendingIntent.getActivity(backService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Notification notify2 = new Notification.Builder(backService.this)
                        .setSmallIcon(R.drawable.notice)
                        .setTicker("next event")
                        .setContentTitle("Event are coming")
                        .setContentText(has+" events are coming within 3 hours")
                        .setContentIntent(pi)
                        .setNumber(1).setDefaults(Notification.DEFAULT_SOUND)
                        .getNotification();
                manger.notify(0, notify2);


            }
            list_near.clear();
            while(it2.hasNext()){
                Thing_data td = it2.next();
                float[] results;
                results = new float[1];
                Location.distanceBetween(location1.getLatitude(), location1.getLongitude(), td.getLatitude(), td.getLongtitude(), results);

                if(results[0] <1000){
                    list_near.add(td.getId());
                    if(results[0] < mindis){
                        id_o_near = td.getId();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(list_near.size()>0){
                NotificationManager manger = (NotificationManager) backService.this.getSystemService(Context.NOTIFICATION_SERVICE);


                Notification notification = new Notification();
                //notification.icon = R.drawable.notice;
                Intent intent = new Intent(backService.this, MainPage.class);


                intent.putExtra("action","nearevent");
                intent.putStringArrayListExtra("list_near",list_near);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pi = PendingIntent.getActivity(backService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Notification notify2 = new Notification.Builder(backService.this)
                        .setSmallIcon(R.drawable.notice)
                        .setTicker("location update")
                        .setContentTitle("Event near you")
                        .setContentText(list_near.size()+" events around you within 1 km")
                        .setContentIntent(pi)
                        .setNumber(1).setDefaults(Notification.DEFAULT_SOUND)
                        .getNotification();
                manger.notify(0, notify2);
            }
            idofnext = id_o_n;
            idofnearest = id_o_near;
            Intent in =new Intent("com.x.yang.thingstodo.NEXTNEARCHANGE");
            in.putExtra("idnear",id_o_near);
            in.putExtra("idnext",id_o_n);
            sendBroadcast(in);
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
            Log.i("GPS_sev","update new location");
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
