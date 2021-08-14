package com.schnell.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PoleDeviceListDAO {

    @Insert
    public void insert( PoleDeviceList... poleDeviceLists);

    @Update
    public void update( PoleDeviceList... ilmAddDevices);

    @Delete
    public void delete( PoleDeviceList ilmAddDevices);

    @Query("SELECT * FROM PoleDeviceList")
    public List< PoleDeviceList> getPoleDevices();

    @Query("SELECT * FROM PoleDeviceList where PoleNumber= :name")
    public List< PoleDeviceList> GetPoleDetails(String name);

    @Query("SELECT * FROM PoleDeviceList where PoleNumber= :name and DeviceNumber=:deviceaddress")
    public List<PoleDeviceList> GetDevicePoleDetails(String name, String deviceaddress);

    @Query("UPDATE PoleDeviceList SET DeviceStatus = :deviceid, DeviceIeeeAddress = :ieeeaddress WHERE DeviceNumber = :devicename")
    void updatePoleilm(String deviceid, String ieeeaddress, String devicename);

    @Query("DELETE FROM PoleDeviceList WHERE PoleNumber= :name and DeviceNumber = :type")
    void DeletePoleDeviceId(String name, String type);

    @Query("DELETE FROM PoleDeviceList")
    void DeletePoledevice();

}
