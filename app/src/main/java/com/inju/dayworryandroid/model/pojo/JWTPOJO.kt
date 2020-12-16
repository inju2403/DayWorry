package com.inju.dayworryandroid.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JWTPOJO (
    @SerializedName("jwt")
    @Expose
    var jwt: String = ""
)