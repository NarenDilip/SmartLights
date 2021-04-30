package com.example.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssetDevicesDAO {

    @Insert
    public void insert(AssetDevices... assetDevices);

    @Update
    public void update(AssetDevices... assetDevices);

    @Delete
    public void delete(AssetDevices assetDevices);

    @Query("SELECT * FROM assetdevice")
    public List<AssetDevices> getsdDevices();

    @Query("SELECT * FROM assetdevice WHERE ccmsname != :type ")
    public List<AssetDevices> getsdlistDevices(String type);

    @Query("SELECT * FROM assetdevice WHERE wardid = :type")
    public List<AssetDevices> getallDevices(String type);

    @Query("SELECT * FROM assetdevice WHERE ccmsid = :type")
    public List<AssetDevices> getccmsDevices(String type);

    @Query("SELECT * FROM assetdevice WHERE ccmsname = :type")
    public List<AssetDevices> getccmsid(String type);

    @Query("UPDATE assetdevice SET ccmsname= :ccmsname , wardid = :wardid  WHERE ccmsid = :type")
    void updatedevicename(String ccmsname, String wardid, String type);

    @Query("DELETE FROM assetdevice")
    void DeleteDetails();
}
