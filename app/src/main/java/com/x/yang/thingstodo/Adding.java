package com.x.yang.thingstodo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class Adding extends FragmentActivity implements DataPickerFragment.DatePick, TimePickFragment.TimePick {
    private ImageButton ib_cam,ib_datepick;
    private ImageButton ib_back,ib_save;
    private ImageView pic;
    private Bitmap bitmap;
    private int day,month,year,hour,min;
    private String date_s,changedID;
    private EditText tit, add, mess;
    private TextView date_t;
    private RadioGroup rg;
    String frequent,addr;
    Alldata ad;
    private boolean isChange = false;
    boolean ready =false;
    double latitude=0.0, longtitude=0.0;
    String id = "";
    GEOTasks tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adding);
        ib_cam = (ImageButton)findViewById(R.id.cam);
        rg =(RadioGroup)findViewById(R.id.radio_add);
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
        tit = (EditText) findViewById(R.id.title_in);
        add = (EditText) findViewById(R.id.add_in);
        add.setOnFocusChangeListener(new addrChange());
        mess = (EditText) findViewById(R.id.mess_in);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {

             int radioButtonId = arg0.getCheckedRadioButtonId();
             RadioButton rb = (RadioButton)Adding.this.findViewById(radioButtonId);
             frequent = rb.getText().toString();
              }
            });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            isChange = true;

            Thing_data temp = ad.getDatafromDB(bundle.getString("id"));

            changedID = bundle.getString("id");
            latitude =temp.getLatitude();
            longtitude = temp.getLongtitude();
            ready = true;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(Environment.getExternalStorageDirectory()+ "/finger/"+changedID+".png");
                bitmap  = BitmapFactory.decodeStream(fis);
                pic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                bitmap = null;
            }
            tit.setText(temp.getTitle());
            add.setText(temp.getLoc());
            mess.setText(temp.getMess());
            year = temp.getYear();
            month = temp.getMonth();
            day = temp.getDay();
            min = temp.getMin();
            hour = temp.getHour();
            date_t.setText(temp.getYear()+"-"+temp.getMonth()+"-"+temp.getDay()+" "+temp.getHour()+":"+temp.getMin());
            if(temp.getFre().contains("once")){
                rg.check(R.id.once);
            }else if(temp.getFre().contains("daily" )|| temp.getFre().contains( "dayly")){
                rg.check(R.id.daily);
            }else{
                rg.check(R.id.monthly);
            }
        }
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


        date_s =year+"-"+(month+1)+"-"+day;

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
        this.min = min;
        if(min < 10){
            date_s = date_s + "  "+hour+" : 0"+min;
        }else{
            date_s = date_s + "  "+hour+" : "+min;
        }
        date_t.setText(date_s);

    }

    private class addrChange implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            String s;
            if(hasFocus == false){
                Log.i("adding","forcechange");
                addr = add.getText().toString();
                ready = false;
                tt=new GEOTasks();
                tt.execute();

            }
        }
    }
    class GEOTasks extends AsyncTask {

        String str = "";

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("cur", "geting geo");


            Geocoder geocoder =new Geocoder(Adding.this);
            try {
                List<Address> l = geocoder.getFromLocationName(addr, 5);
                if(l.size()>0){
                    if(l.size() != 1){
                        str = "We find more than 1 location. Please including city, province and country to boost accuracy";
                        //Toast.makeText(Adding.this,"We find more than 1 location. Please including city, province and country to boost accuracy",Toast.LENGTH_LONG).show();
                    }
                        latitude = l.get(0).getLatitude();
                        longtitude = l.get(0).getLongitude();
                    Log.i("",""+latitude);
                        ready = true;

                }else{
                    //ready = false;
                    str ="warning: your address may be not correct, please check it";
                    ready = true;
                    //Toast.makeText(Adding.this,"your address is not correct, please check it",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                //ready = false;
                //e.printStackTrace();
                str ="your address is not correct, please check it";
                //Toast.makeText(Adding.this,"your address is not correct, please check it",Toast.LENGTH_LONG).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(str !="")
            Toast.makeText(Adding.this,str,Toast.LENGTH_LONG).show();
        }
    }


    private class butClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.back:
                    Log.i("keys", "back");
                    if(changedID != null){
                        setResult(RESULT_CANCELED);
                    }
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
                    break;
                case R.id.save:
                    if(frequent == null){
                        Toast.makeText(Adding.this,"you need to select the frequent of this event",Toast.LENGTH_SHORT).show();
                    } else if(!ready){
                        Toast.makeText(Adding.this,"Location problem, System will relocate after a while",Toast.LENGTH_SHORT).show();
                        ready = true;
                    } else{
                        ad.deleteData(changedID);

                        saveEvent();
                        for(int i=0;i<4000;i++);

                        Intent in2 =new Intent("com.x.yang.thingstodo.NEWEVENT_S");
                        sendBroadcast(in2);
                        if(changedID != null){

                            Intent in3 =new Intent("com.x.yang.thingstodo.DETAIL.UPDATE");
                            in3.putExtra("id",id);
                            sendBroadcast(in3);
                        }


                        Adding.this.finish();

                    }
                    break;


            }
        }
    }

    private void saveEvent(){
        Calendar c = Calendar.getInstance();

        if(changedID != null){
            id = changedID;
        }else {
            id = "2" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND);
        }
        String title = tit.getText().toString();
        String addr = add.getText().toString();
        String message = mess.getText().toString();
        Location l = times.getloc();

        // save pic....................../
        if(bitmap !=null){
        byte []data;
        String filename = id + ".png";
        Log.i("adding","addphoto");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        data = baos.toByteArray();
        File fileFolder = new File(Environment.getExternalStorageDirectory()
                + "/finger/");
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(jpgFile);
            outputStream.write(data);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){

        }
        }
        //save database............./

        ad.setDate(title,message,id,addr,longtitude,latitude,year,month,day,hour,min,frequent);


    }
}
