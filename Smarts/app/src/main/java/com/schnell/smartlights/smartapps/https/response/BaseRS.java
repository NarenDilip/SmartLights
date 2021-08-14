package com.schnell.smartlights.smartapps.https.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by androiduser2 on 27/8/15.
 */
public class BaseRS {

    @SerializedName("RESPONSECODE")
    public String responseCode;

    @SerializedName("ERRORCODE")
    public String errorCode;

    @SerializedName("token")
    public String usrtoken;

    @SerializedName("error_code")
    public String err_code;

    @SerializedName("refresh_token")
    public String refresh_token;

    @SerializedName("usr_group_id")
    public String entityId;

    @SerializedName("usr_id")
    public String user_Id;

    @SerializedName("otp")
    public String user_otp;

}
