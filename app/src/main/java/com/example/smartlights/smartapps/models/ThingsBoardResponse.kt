package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

open class ThingsBoardResponse : Response() {

    @SerializedName("createdTime")
    var createdTime: Long = 0

    @SerializedName("id")
    var id: Entity? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("tenantId")
    var tenantId: Entity? = null

    @SerializedName("customerId")
    var customerId: Entity? = null

    @SerializedName("ownerId")
    var ownerId: Entity? = null

    @SerializedName("deviceIndex")
    var deviceIndex = 0

    @SerializedName("additionalInfo")
    var additionalInfo: AdditionalInfo? = AdditionalInfo()

    @SerializedName("email")
    var email: String? = null

    @SerializedName("authority")
    var authority: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("lastUpdateTs")
    var tsdata: String? = null

    @SerializedName("key")
    var devicekey: String? = null

    @SerializedName("ts")
    var names: String? = null

    @SerializedName("value")
    var value: String? = null

    @SerializedName("alert")
    var historyList: ArrayList<ThingsBoardResponse>? = null

    @SerializedName("data")
    var devicesList: ArrayList<ThingsBoardResponse>? = null
}