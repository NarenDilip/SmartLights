package com.schnell.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ilmAddDeviceDAO {

    @Insert
    public void insert( ilmAddDevice... ilmAddDevices);

    @Update
    public void update( ilmAddDevice... ilmAddDevices);

    @Delete
    public void delete( ilmAddDevice ilmAddDevices);

    @Query("SELECT * FROM ilmAddDevice")
    public List< ilmAddDevice> getDevices();

    @Query("SELECT * FROM ilmAddDevice where devicename= :name")
    public List< ilmAddDevice> GetDevices(String name);

    @Query("SELECT * FROM ilmAddDevice where PoleId= :type")
    public List< ilmAddDevice> Devices(String type);

    @Query("SELECT * FROM ilmAddDevice where PoleId= :type and ilmnumber =:ilmNumber")
    public List< ilmAddDevice> DevicesDetails (String type, String ilmNumber);

    @Query("UPDATE ilmAddDevice SET deviceId = :deviceid, ieeeAddress = :ieeeaddress WHERE devicename = :devicename")
    void updateilm(String deviceid, String ieeeaddress, String devicename);

    @Query("DELETE FROM ilmAddDevice WHERE PoleId = :type")
    void DeleteId(String type);

    @Query("DELETE FROM ilmAddDevice WHERE PoleId = :type and devicename = :name")
    void DeletedeviceId(String type, String name);

    @Query("DELETE FROM ilmAddDevice WHERE devicename = :name")
    void Deletedevice(String name);

    @Query("DELETE FROM ilmAddDevice")
    void DeleteListdevice();

}
