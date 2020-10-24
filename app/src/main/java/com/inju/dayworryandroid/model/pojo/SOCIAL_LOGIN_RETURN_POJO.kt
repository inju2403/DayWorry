package com.inju.dayworryandroid.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SOCIAL_LOGIN_RETURN_POJO(
    @SerializedName("jwt")
    @Expose
    var jwt: String = "",

    @SerializedName("status")
    @Expose
    var status: String = ""
)