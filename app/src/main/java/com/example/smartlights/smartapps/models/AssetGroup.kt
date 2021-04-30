package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

class AssetGroup : Response() {

    @SerializedName("createdTime")
    var createdTime: Long = 0

    @SerializedName("id")
    var id: Entity? = null

    @SerializedName("tenantId")
    var tenantId: Entity? = null

    @SerializedName("customerId")
    var customerId: Entity? = null

    @SerializedName("ownerId")
    var ownerId: Entity? = null

    @SerializedName("additionalInfo")
    var additionalInfo: AdditionalInfo? = AdditionalInfo()

    @SerializedName("type")
    var type: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("data")
    var deviceList: ArrayList<AssetGroup>? = null
}