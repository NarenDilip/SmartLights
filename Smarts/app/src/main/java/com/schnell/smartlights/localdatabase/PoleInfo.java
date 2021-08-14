package com.schnell.smartlights.localdatabase;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Poleinfo", indices = {@Index(value = "Id", unique = true)})
public class PoleInfo {

    @PrimaryKey(autoGenerate = true)
    private int Sno;
    private String Id;
    private String PoleName;
    private String Poletype;
    private String PoleId;

    public int getSno() {
        return Sno;
    }

    public void setSno(int sno) {
        Sno = sno;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPoleName() {
        return PoleName;
    }

    public void setPoleName(String poleName) {
        PoleName = poleName;
    }

    public String getPoletype() {
        return Poletype;
    }

    public void setPoletype(String poletype) {
        Poletype = poletype;
    }

    public String getPoleId() {
        return PoleId;
    }

    public void setPoleId(String poleId) {
        PoleId = poleId;
    }
}
