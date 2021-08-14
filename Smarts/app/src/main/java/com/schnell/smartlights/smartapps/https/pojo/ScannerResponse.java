package com.schnell.smartlights.smartapps.https.pojo;

import com.schnell.smartlights.smartapps.https.response.BaseRS;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScannerResponse extends BaseRS implements Serializable {

    @SerializedName("boardNumber")
    public String BoardNumber;

    @SerializedName("credentialsId")
    public String Credentials;

    @SerializedName("id")
    public String Id;

    @SerializedName("dispatch_id")
    public String despatchid;

    @SerializedName("panel_uid")
    public String panelid;

}
