package com.x.yang.thingstodo;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by yang on 2016/4/6.
 */
public class Alldata {
    private FileIssue fi;
    static Context cx;
    public int num_things;
    public int num_today;
    public ArrayList<Thing_data> list;
    private SQLiteDatabase db;
    private static boolean is =false;
    private  static Alldata ourInstance ;

    public  static Alldata getInstance(Context c) {

        if(ourInstance !=null && is){
            return ourInstance;
        }else{
            is = true;
            cx = c;
            ourInstance = new Alldata(c);
        }

        return ourInstance;
    }

    private Alldata(Context cc) {
        Log.i("SQL","has"+num_things);
        num_things=0;
        num_today=0;
        list = new ArrayList<Thing_data>();
        fi = new FileIssue(cc);
        db = fi.getReadableDatabase();
        db.beginTransaction();
        long date;
        long time;
        SimpleDateFormat format;
        TimeZone tz =TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance();

        try {
            Cursor c = db.rawQuery("SELECT * FROM things", null);
            num_things = c.getCount();
            Log.i("SQL","has"+num_things);
            Cursor c2 = db.rawQuery("SELECT * FROM things where year ="+calendar.get(Calendar.YEAR)+" and month ="+calendar.get(Calendar.MONTH)+" and day ="+calendar.get(Calendar.DAY_OF_MONTH), null);
            num_today = c2.getCount();
            readingData ed =new readingData();
            ed.execute();

            c.close();
        }catch (Exception e){


        }
        db.endTransaction();

    }
    public void setDate(String title, String mess,String id,String addr, double longt, double lati, int year, int month, int day, int hour, int min, String freq){

        SQLiteDatabase db1 = fi.getWritableDatabase();

        db1.execSQL("insert into things values('"+id+"','"+title+"','"+addr+"',"+lati+","+longt+","+year+","+month+","+day+","+hour+","+min+",'"+freq+"','"+mess+"')");

        db = fi.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery("SELECT * FROM things", null);
        num_things = c.getColumnCount();
        Log.i("SQL","has"+num_things);
        db.endTransaction();

    }

    public int getNumAll(){
        int opt = 0;

        return opt;
    }

    private class readingData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("mess","i am here");
            Cursor c = db.rawQuery("SELECT * FROM things", null);
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
                // format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                // Date dated = new Date(timestamp * 1000);
                //thing.setThingdate(sdf.format(dated));

                //thing.setThingdate((Date) format.parse(str));

                list.add(thing);
            }
            c.close();
            Log.i("mess","i am here2");
            return null;
        }
    }
}
