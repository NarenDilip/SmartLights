package com.example.smartlights.localdatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AssetDevice")
public class AssetDevices {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String wardid;
    private String ccmsid;
    private String ccmsname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
    }

    public String getCcmsid() {
        return ccmsid;
    }

    public void setCcmsid(String ccmsid) {
        this.ccmsid = ccmsid;
    }

    public String getCcmsname() {
        return ccmsname;
    }

    public void setCcmsname(String ccmsname) {
        this.ccmsname = ccmsname;
    }
}
