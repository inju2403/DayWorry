package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User_REQUEST_POJO(
    @SerializedName("ageRange")
    @Expose
    var ageRange: Int = 0,

    @SerializedName("nickname")
    @Expose
    var nickname: String = "",

    @SerializedName("profileImage")
    @Expose
    var profileImage: String = "",

    @SerializedName("userHashtags")
    @Expose
    var userHashtags: MutableList<String> = mutableListOf(),

    @SerializedName("userId")
    @Expose
    var userId: Int = 0
)