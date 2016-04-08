package com.x.yang.thingstodo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by yang on 2016/4/7.
 */
public class LocateSpot extends HandlerThread {
    Handler gpshandle;
    private final int MES_UPDATE = 0;
    public LocateSpot(String name) {
        super(name);

    }

    @Override
    protected void onLooperPrepared(){
        gpshandle = new Handler(){
            public void handleMessage(Message mes){
                if(mes.what == MES_UPDATE){
                    newlocation();
                }
            }

        };
    }

    private void newlocation(){

    }

    public void queuelocation(){

    }
}
