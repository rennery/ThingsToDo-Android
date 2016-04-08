package com.x.yang.thingstodo;

import android.location.Location;

/**
 * Created by Yang on 2016/1/1.
 */
public class times {

    int t;
    private static Location l;

    public static Location getloc(){
        if(l == null){
            return null;
        }else{
            return l;
        }
    }
    public static void setL(Location ll){
        l = ll;
    }
}
