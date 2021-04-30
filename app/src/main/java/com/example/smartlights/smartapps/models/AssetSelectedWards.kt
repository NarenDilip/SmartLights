package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.example.smartlights.smartapps.models.AdditionalInfo
import com.google.gson.annotations.SerializedName

class AssetSelectedWards : Response() {

    @SerializedName("from")
    var from_id: Entity? = null

    @SerializedName("to")
    var to_id: Entity? = null

    @SerializedName("additionalInfo")
    var additionalInfo: AdditionalInfo? = AdditionalInfo()

    @SerializedName("type")
    var type: String? = null

    @SerializedName("typeGroup")
    var typeGroup: String? = null

    @SerializedName("data")
    var deviceList: ArrayList<AssetSelectedWards>? = null
}