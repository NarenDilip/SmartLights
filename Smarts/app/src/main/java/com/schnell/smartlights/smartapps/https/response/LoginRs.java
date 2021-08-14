package com.schnell.smartlights.smartapps.https.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by narendran on 7/7/16.
 */
public class LoginRs implements Serializable {

    @SerializedName("PROJECT_ID")
    public String projectID;
    @SerializedName("ID")
    public String id;
    @SerializedName("PROJECT_NAME")
    public String projectName;
}
