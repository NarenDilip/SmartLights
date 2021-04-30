package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

class LoginResponse : Response() {

    @SerializedName("token")
    var token: String? = null

    @SerializedName("refreshToken")
    var refreshToken: String? = null

}