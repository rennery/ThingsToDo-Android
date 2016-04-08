package com.x.yang.thingstodo;

import java.util.Date;

/**
 * Created by yang on 2016/4/6.
 */
public class Thing_data {

    private int id;
    private String loc;
    private double latitude;
    private double longtitude;
    private long thingdate;

    public long getThingtime() {
        return thingtime;
    }

    public void setThingtime(long thingtime) {
        this.thingtime = thingtime;
    }

    private long thingtime;

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    private String mess;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getThingdate() {
        return thingdate;
    }

    public void setThingdate(long thingdate) {
        this.thingdate = thingdate;
    }


}
