package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

open class DeviceCredential: Response() {

    @SerializedName("createdTime")
    var createdTime: Long = 0
    @SerializedName("id")
    var id: Entity? = null
    @SerializedName("deviceId")
    var deviceId: Entity? = null
    @SerializedName("credentialsId")
    var credentialsId: String? = null
    @SerializedName("credentialsType")
    var credentialsType: String? = "ACCESS_TOKEN"
    @SerializedName("credentialsValue")
    var credentialsValue: String? = null
}