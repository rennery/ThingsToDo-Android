package com.x.yang.thingstodo;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    private static Alldata ourInstance ;

    public static Alldata getInstance(Context c) {

        if(ourInstance !=null){
            return ourInstance;
        }else{
            ourInstance = new Alldata(c);
        }

        return ourInstance;
    }

    private Alldata(Context cc) {
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

        try {
            Cursor c = db.rawQuery("SELECT * FROM things", null);
            num_things = c.getCount();
            while (c.moveToNext()) {
                Thing_data thing = new Thing_data();
                thing.setId(c.getInt(c.getColumnIndex("id")));
                thing.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
                thing.setLoc(c.getString(c.getColumnIndex("location")));
                thing.setLongtitude(c.getDouble(c.getColumnIndex("longtitude")));
                thing.setMess(c.getString(c.getColumnIndex("message")));
                date = c.getInt(c.getColumnIndex("thingdate"));
                time = c.getInt(c.getColumnIndex("thingtime"));
                thing.setThingdate(date);
                thing.setThingtime(time);
               // format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
               // Date dated = new Date(timestamp * 1000);
                //thing.setThingdate(sdf.format(dated));

                //thing.setThingdate((Date) format.parse(str));

                list.add(thing);
            }
            c.close();
        }catch (Exception e){


        }

    }
    public void setDate(String location, String mess){

    }

    public int getNumAll(){
        int opt = 0;

        return opt;
    }
}
