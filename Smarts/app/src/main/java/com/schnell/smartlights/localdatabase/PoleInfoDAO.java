package com.schnell.smartlights.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PoleInfoDAO {

    @Insert
    public void insert(PoleInfo... poleInfos);

    @Update
    public void update(PoleInfo... poleInfos);

    @Delete
    public void delete(PoleInfo poleInfos);

    @Query("SELECT * FROM Poleinfo")
    public List<PoleInfo> getDevices();

    @Query("SELECT * FROM Poleinfo where PoleName = :name")
    public List<PoleInfo> getPole(String name);

    @Query("DELETE FROM Poleinfo WHERE PoleName = :name")
    void DeleteId(String name);

    @Query("DELETE FROM Poleinfo")
    void DeletePole();
}
