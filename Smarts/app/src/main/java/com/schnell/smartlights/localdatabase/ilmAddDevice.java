package com.schnell.smartlights.localdatabase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ilmAddDevice")
public class ilmAddDevice {

    @PrimaryKey(autoGenerate = true)
    private int Sno;
    private String PoleId;
    private String Landmark;
    private String Lat;
    private String Lon;
    private String Poletype;
    private String Lampwatts;
    private String devicename;
    private String ilmnumber;
    private String deviceId;
    private String ieeeAddress;

    public int getSno() {
        return Sno;
    }

    public void setSno(int sno) {
        Sno = sno;
    }

    public String getPoleId() {
        return PoleId;
    }

    public void setPoleId(String poleId) {
        PoleId = poleId;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getPoletype() {
        return Poletype;
    }

    public void setPoletype(String poletype) {
        Poletype = poletype;
    }

    public String getLampwatts() {
        return Lampwatts;
    }

    public void setLampwatts(String lampwatts) {
        Lampwatts = lampwatts;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getIlmnumber() {
        return ilmnumber;
    }

    public void setIlmnumber(String ilmnumber) {
        this.ilmnumber = ilmnumber;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIeeeAddress() {
        return ieeeAddress;
    }

    public void setIeeeAddress(String ieeeAddress) {
        this.ieeeAddress = ieeeAddress;
    }
}
