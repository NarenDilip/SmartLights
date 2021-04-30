package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

class FromAddress : Response() {

    @SerializedName("from")
    var from: Entity? = null

    @SerializedName("to")
    var to: Entity? = null

    @SerializedName("fromName")
    var fromName: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("data")
    var fromList: ArrayList<FromAddress>? = null

}