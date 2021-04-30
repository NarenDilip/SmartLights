package com.example.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ZoneDevicesDAO {

    @Insert
    public void insert(ZoneDevices... zoneDevices);

    @Update
    public void update(ZoneDevices... zoneDevices);

    @Delete
    public void delete(ZoneDevices zoneDevices);

    @Query("SELECT * FROM ZoneDevices")
    public List<ZoneDevices> getDevices();

    @Query("SELECT * FROM ZoneDevices WHERE Deviceid= :type")
    public List<ZoneDevices> getZDevices(String type);

    @Query("SELECT * FROM ZoneDevices WHERE Deviceid= :type AND Regionname= :rtype")
    public List<ZoneDevices> getZsDevices(String type, String rtype);

    @Query("SELECT * FROM ZoneDevices WHERE Regionname = :type")
    public List<ZoneDevices> getRegionDevices(String type);

    @Query("SELECT * FROM ZoneDevices WHERE Devicename = :type")
    public List<ZoneDevices> getNUllDevices(String type);

    @Query("UPDATE ZoneDevices SET Devicename= :deviceloader, Devicetype = :devicetype WHERE deviceid = :type")
    void updatedevicezone(String deviceloader, String devicetype, String type);

    @Query("DELETE FROM ZoneDevices")
    void DeleteZone();

}
