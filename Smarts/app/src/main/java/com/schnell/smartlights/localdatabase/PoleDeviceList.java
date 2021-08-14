package com.schnell.smartlights.localdatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PoleDeviceList")
public class PoleDeviceList {

    @PrimaryKey(autoGenerate = true)
    private int Sno;
    private String PoleNumber;
    private String Region;
    private String Zone;
    private String Ward;
    private String DeviceNumber;
    private String DeviceId;
    private String DeviceStatus;
    private String DeviceIeeeAddress;

    private String Landmark;
    private String noofarms;
    private String lampwatts;
    private String lifetime;
    private String scnumber;

    public int getSno() {
        return Sno;
    }

    public void setSno(int sno) {
        Sno = sno;
    }

    public String getPoleNumber() {
        return PoleNumber;
    }

    public void setPoleNumber(String poleNumber) {
        PoleNumber = poleNumber;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getDeviceStatus() {
        return DeviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        DeviceStatus = deviceStatus;
    }

    public String getDeviceIeeeAddress() {
        return DeviceIeeeAddress;
    }

    public void setDeviceIeeeAddress(String deviceIeeeAddress) {
        DeviceIeeeAddress = deviceIeeeAddress;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getNoofarms() {
        return noofarms;
    }

    public void setNoofarms(String noofarms) {
        this.noofarms = noofarms;
    }

    public String getLampwatts() {
        return lampwatts;
    }

    public void setLampwatts(String lampwatts) {
        this.lampwatts = lampwatts;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public String getScnumber() {
        return scnumber;
    }

    public void setScnumber(String scnumber) {
        this.scnumber = scnumber;
    }
}
