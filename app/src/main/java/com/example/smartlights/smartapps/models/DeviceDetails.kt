package com.example.smartlights.smartapps.models

import com.google.gson.annotations.SerializedName

class DeviceDetails: ThingsBoardResponse() {

//    @SerializedName("name")
//    var name: String? = null
//
//    @SerializedName("type")
//    var type: String? = null

    @SerializedName("label")
    var label: String? = null

}

