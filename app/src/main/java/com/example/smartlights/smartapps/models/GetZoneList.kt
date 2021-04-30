package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

class GetZoneList : Response() {

    @SerializedName("from")
    var fromentity: Entity? = null

    @SerializedName("to")
    var toentity: Entity? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("typeGroup")
    var typeGroup: String? = null

    @SerializedName("additionalInfo")
    var additInfo: String? = null

    @SerializedName("data")
    var deviceList: ArrayList<GetZoneList>? = null
}

