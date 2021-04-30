package com.example.smartlights.smartapps.models

import com.example.smartlights.services.Response
import com.google.gson.annotations.SerializedName

class addDetails : Response(){

    @SerializedName("description")
    var description: String? = null
}
