package com.example.smartlights.smartapps.models

import com.google.gson.annotations.SerializedName

class IndVal : ThingsBoardResponse() {

    @SerializedName("onCCMS")
    var onCCMS: ArrayList<ThingsBoardResponse>? = null

    @SerializedName("offCCMS")
    var offCCMS: ArrayList<ThingsBoardResponse>? = null

    @SerializedName("offlineCCMS")
    var offlineCCMS: ArrayList<ThingsBoardResponse>? = null

    @SerializedName("totalCCMS")
    var totalCCMS: ArrayList<ThingsBoardResponse>? = null

}