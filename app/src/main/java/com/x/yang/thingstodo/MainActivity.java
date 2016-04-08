package com.x.yang.thingstodo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {

   private times count_down;
   private ImageView iv_welcome;
   private TimerTasks tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_welcome=(ImageView)findViewById(R.id.im_welcome);
        iv_welcome.setImageResource(R.drawable.welcome1);
        Alldata.getInstance(this);



        tt=new TimerTasks();
        count_down=new times();
        tt.execute();

        Log.i("value of time countdown", Integer.toString(count_down.t));




    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tt.cancel(true);
     }
    class TimerTasks extends AsyncTask{
        TimerTask task;

        @Override
        protected Object doInBackground(Object[] params) {
            Timer t =new Timer();
            task=new TimerTask(){
               public void run(){
                   startActivity(new Intent(MainActivity.this, MainPage.class));
                   MainActivity.this.finish();
               }
            };
            long k=3000L;

            t.schedule(task,k);
            return null;
        }
    }

    class times_create implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(MainActivity.this,MainPage.class));
            MainActivity.this.finish();

        }
    }



}
