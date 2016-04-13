package com.x.yang.thingstodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainPage extends FragmentActivity {

    final int RIGHT = 0;
    public static String city="locating...";
    public static String state="";
    final int LEFT = 1;
    private TimerTasks tt;
    private LocateSpot ls;

    private android.support.v4.app.FragmentManager fm;
    private FragmentTabHost TabHost;
    private Class fragmentsArray[] ={
            MainPageFragment.class,fregrement_list.class, fragment_message.class,frgrement_settings.class
    };
    private String tabString[] ={"main","list","detail","settings"};

    private int icons[]={R.drawable.main_icon,R.drawable.list_icon,R.drawable.message_icon,R.drawable.setting_icon};
    private GestureDetector gestureDetector;
    private FrameLayout fl;
    changeReceiver receiver;

    Fragment f_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fm=getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initView();
        receiver=new changeReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.CHANGE");
        this.registerReceiver(receiver, filter);

        TabHost.setCurrentTab(0);
        Log.i("currenttab", TabHost.getCurrentTabTag());
        Intent inte=new Intent(this,backService.class);
        startService(inte);
        tt=new TimerTasks();
        tt.execute();
        ls = new LocateSpot("GPS");
        ls.start();
        ls.getLooper();
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            if(bundle.getString("action") == "timeup"){
                TabHost.setCurrentTab(2);
                Intent intent = new Intent("com.x.yang.DETAIL.SINGLE");
                intent.putExtra("id",bundle.getString("id"));
                sendBroadcast(intent);
            }
        }
        LocationManager locationManager=(LocationManager)MainPage.this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        Location location=locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 1000*60, 100, locationListener);



    }
    private final LocationListener locationListener = new LocationListener()
    {
        public void onLocationChanged(Location location) {
            times.setL(location);
            updateLoc();
            Intent intnet = new Intent("com.x.yang.thingstodo.GPSREADY");
            Log.i("GPS","update new location");
            sendBroadcast(intnet);
            Toast.makeText(MainPage.this,"location changed",Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainPage.this,"please open your locate function",Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainPage.this,"locate function enabled",Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

            if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE){

            }
        }
    };


    private void initView(){
        int i;

        TabHost=(FragmentTabHost)findViewById(R.id.tabhost);
        TabHost.setup(this, fm, R.id.tabcontent);

        for(i=0;i<fragmentsArray.length;i++){
            TabSpec tabspec= TabHost.newTabSpec(tabString[i]);
            tabspec.setIndicator(tabViewAdapter(i));
            TabHost.addTab(tabspec, fragmentsArray[i], null);
            TabHost.getTabWidget().getChildTabViewAt(i).setBackgroundResource(R.drawable.tab_background);
        }
    }

    private View tabViewAdapter(int i){
        View v;
        v= View.inflate(this,R.layout.icon_tab,null);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageview);
        imageView.setImageResource(icons[i]);
        TextView textView = (TextView) v.findViewById(R.id.textview);
        textView.setText(tabString[i]);


        return v;
    }
    private class changeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            if(b.getInt("tab") ==0){
                TabHost.setCurrentTab(0);
            }else if (b.getInt("tab") ==1){
                TabHost.setCurrentTab(1);
            }else if (b.getInt("tab") ==2){
                TabHost.setCurrentTab(2);
            }else if (b.getInt("tab") ==3){
                TabHost.setCurrentTab(3);
            }
            Intent in = new Intent("com.x.yang.thingstodo.ACTION");
            in.putExtra("act",b.getString("action"));
            sendBroadcast(in);
        }
    }
    private void updateLoc(){
        Location location = times.getloc();
        try{


            Geocoder geo = new Geocoder(MainPage.this, Locale.getDefault());

            Log.i("currenttab", Double.toString(location.getAltitude()));
            List<Address> addresses = geo.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Log.i("currenttab", "22333");
            if (addresses.isEmpty()) {
                Toast.makeText(MainPage.this, "Can't locate your city,please chcek setting of GPS", Toast.LENGTH_LONG).show();
                Log.i("error","GPS");
            }
            else {
                if (addresses.size() > 0) {
                    MainPage.city  =addresses.get(0).getLocality();
                    MainPage.state  =addresses.get(0).getAdminArea();
                    Intent intnet = new Intent("com.x.yang.thingstodo.GPSREADY");
                    sendBroadcast(intnet);

                    Log.i("location", addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                }
            }
        }catch(Exception e){


        }
    }



    class TimerTasks extends AsyncTask {
        TimerTask task;

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("cur", "12444333");

            LocationManager locationManager=(LocationManager)MainPage.this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            String provider = locationManager.getBestProvider(criteria, true);
            Location location=locationManager.getLastKnownLocation(provider);
            Log.i("cur", "12333");
            times.setL(location);
            updateLoc();
            long k=3000L;

            return null;
        }
    }

    @Override
    protected void onDestroy() {
        ls.quit();
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
