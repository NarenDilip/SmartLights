package com.example.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DDataDAO {

    @Insert
    public void insert(DData... dData);

    @Update
    public void update(DData... dData);

    @Delete
    public void delete(DData dData);

    @Query("SELECT * FROM DData")
    public List<DData> getsdDevices();

    @Query("SELECT * FROM DData WHERE Deviceid = :type")
    public List<DData> getallDevices(String type);

    @Query("DELETE FROM DData")
    void DeleteR();
}
