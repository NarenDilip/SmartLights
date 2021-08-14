package com.schnell.smartlights.smartapps.https.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by narendran on 7/7/16.
 */
public class LoginBaseRs extends BaseRS implements Serializable {

    @SerializedName("DETAILS")
    public LoginRs loginObj;
}
