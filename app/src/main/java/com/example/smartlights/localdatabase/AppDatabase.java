package com.example.smartlights.localdatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {RegionDevices.class, ZoneDevices.class, WardDevices.class, DData.class,AssetDevices.class}, version = 1)
@TypeConverters({DateTypeConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract RegionDevicesDAO getDeviceDAO();

    public abstract ZoneDevicesDAO getZoneDeviceDAO();

    public abstract WardDevicesDAO getWardNumbersDAO();

    public abstract DDataDAO getData();

    public abstract AssetDevicesDAO devicesDAO();


}
