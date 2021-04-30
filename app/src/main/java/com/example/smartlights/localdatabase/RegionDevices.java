package com.example.smartlights.localdatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RegionDevices")

public class RegionDevices {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Deviceid;
    private String Devicename;
    private String Devicetype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceid() {
        return Deviceid;
    }

    public void setDeviceid(String deviceid) {
        Deviceid = deviceid;
    }

    public String getDevicename() {
        return Devicename;
    }

    public void setDevicename(String devicename) {
        Devicename = devicename;
    }


    public String getDevicetype() {
        return Devicetype;
    }

    public void setDevicetype(String devicetype) {
        Devicetype = devicetype;
    }
}
