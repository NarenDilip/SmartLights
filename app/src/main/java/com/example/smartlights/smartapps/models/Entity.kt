package com.example.smartlights.smartapps.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Entity : Serializable {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("entityType")
    var entityType: String? = null

}