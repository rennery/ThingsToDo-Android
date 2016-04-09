package com.x.yang.thingstodo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class Adding extends FragmentActivity implements DataPickerFragment.DatePick, TimePickFragment.TimePick {
    private ImageButton ib_cam,ib_datepick;
    private ImageButton ib_back,ib_save;
    private ImageView pic;
    private Bitmap bitmap;
    private int day,month,year,hour,min;
    private TextView date_t,time_t;
    private Date time;
    Alldata ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
        ib_cam = (ImageButton)findViewById(R.id.cam);
        ib_datepick = (ImageButton)findViewById(R.id.pick);
        ib_back = (ImageButton)findViewById(R.id.back);
        ib_save = (ImageButton)findViewById(R.id.save);
        ib_back.setOnClickListener(new butClick());
        ib_datepick.setOnClickListener(new butClick());
        ib_save.setOnClickListener(new butClick());
        ib_cam.setOnClickListener(new butClick());
        pic = (ImageView)findViewById(R.id.pic);
        ad =Alldata.getInstance(this);
        date_t = (TextView)findViewById(R.id.dateofevent);
        time_t =(TextView)findViewById(R.id.timeofevent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");
        Log.i("cam","get pic");
        pic.setImageBitmap(bitmap);
    }

    @Override
    public void InputComplete(int day, int month,int year) {
        this.day = day;
        this.month = month;
        this.year = year;

        date_t.setText(year + "-" + month + "-" + day);
        Bundle bd = new Bundle();
        bd.putSerializable("Time of use", "0");
        TimePickFragment tpf = new TimePickFragment();
        tpf.setArguments(bd);
        android.support.v4.app.FragmentManager fragmentm = Adding.this.getSupportFragmentManager();
        tpf.show(fragmentm, "DATE");


    }

    @Override
    public void InputComplete_t(int hour, int min) {
        this.hour = hour;
        this.min =min;
        if(min < 10){
            time_t.setText(hour+" : 0"+min);
        }else{
            time_t.setText(hour+" : "+min);
        }

    }


    private class butClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.back:
                    Log.i("keys", "back");
                    Adding.this.finish();
                    break;
                case R.id.cam:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.pick:
                    Bundle bd = new Bundle();
                    bd.putSerializable("Time of use", "0");
                    DataPickerFragment dpf = new DataPickerFragment();
                    TimePickFragment tpf = new TimePickFragment();
                    tpf.setArguments(bd);
                    dpf.setArguments(bd);
                    android.support.v4.app.FragmentManager fragmentm = Adding.this.getSupportFragmentManager();
                    //dpf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    dpf.show(fragmentm,"DATE");


            }
        }
    }
}
