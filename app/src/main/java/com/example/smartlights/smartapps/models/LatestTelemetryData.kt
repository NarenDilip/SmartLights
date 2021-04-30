package com.example.smartlights.smartapps.models

import com.google.gson.annotations.SerializedName

class LatestTelemetryData : ThingsBoardResponse() {

    @SerializedName("lastUpdateTs")
    var teledate: String? = null

    @SerializedName("key")
    var telkey: String? = null

    @SerializedName("value")
    var telvalue: String? = null

    @SerializedName("data")
    var telemetrylist: ArrayList<LatestTelemetryData>? = null

}