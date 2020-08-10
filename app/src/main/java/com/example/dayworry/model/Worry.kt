package com.example.dayworry.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Worry (
    @SerializedName("content")
    @Expose
    var content: String = ""
)