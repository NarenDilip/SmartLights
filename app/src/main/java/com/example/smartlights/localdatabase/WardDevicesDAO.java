package com.example.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WardDevicesDAO {

    @Insert
    public void insert(WardDevices... wardDevices);

    @Update
    public void update(WardDevices... wardDevices);

    @Delete
    public void delete(WardDevices wardDevices);

    @Query("SELECT * FROM WardDevices")
    public List<WardDevices> getwardDevices();

    @Query("SELECT * FROM WardDevices where Devicename = :type")
    public List<WardDevices> getsdwardDevices(String type);

    @Query("SELECT * FROM WardDevices WHERE Deviceid = :type")
    public List<WardDevices> getwDevices(String type);

    @Query("SELECT * FROM WardDevices WHERE Devicename = :devicename")
    public List<WardDevices> getwardsDevices(String devicename);

    @Query("SELECT * FROM WardDevices WHERE Devicename = :type AND Deviceid = :did")
    public List<WardDevices> getasswDevices(String type, String did);

    @Query("SELECT * FROM WardDevices WHERE Deviceid = :type AND Zonename = :ztype")
    public List<WardDevices> getwSDevices(String type, String ztype);

    @Query("SELECT * FROM WardDevices WHERE Zonename= :type")
    public List<WardDevices> getZoneDevices(String type);

    @Query("UPDATE WardDevices SET Devicename= :deviceloader , Devicetype = :devicetype, Zonename = :zone WHERE deviceid = :type")
    void updatedeviceward(String deviceloader, String devicetype, String zone, String type);

    @Query("DELETE FROM WardDevices")
    void DeleteWard();
}
