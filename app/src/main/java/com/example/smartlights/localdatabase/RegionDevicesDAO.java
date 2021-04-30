package com.example.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RegionDevicesDAO {

    @Insert
    public void insert(RegionDevices... regionDevices);

    @Update
    public void update(RegionDevices... regionDevices);

    @Delete
    public void delete(RegionDevices regionDevices);

    @Query("SELECT * FROM RegionDevices")
    public List<RegionDevices> getDevices();

    @Query("SELECT * FROM RegionDevices WHERE Devicename = :type")
    public List<RegionDevices> getSelectedname(String type);

    @Query("SELECT * FROM RegionDevices WHERE Deviceid = :type")
    public List<RegionDevices> getSelectedDevices(String type);

    @Query("DELETE FROM RegionDevices")
    void DeleteRegion();

}
