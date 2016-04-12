package com.x.yang.thingstodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yang on 2016/4/6.
 */
public class FileIssue extends SQLiteOpenHelper{

    private static final String DB_name = "Things_XYang";
    private static final int VERSION = 2;
    public FileIssue(Context context) {
        super(context, DB_name, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS things(id varcgar primary key, title varchar, location varchar, latitude real, longtitude real, year integer ,month integer, day integer, hour integer, min integer, repeat varchar, messageadte varchar)");
        Log.i("Database", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
