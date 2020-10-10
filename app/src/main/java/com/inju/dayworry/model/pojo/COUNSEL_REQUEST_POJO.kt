package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class COUNSEL_REQUEST_POJO (
    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("userId")
    @Expose
    var userId: Long = 0,

    @SerializedName("postId")
    @Expose
    var postId: Long = 0,

    @SerializedName("profileImage")
    @Expose
    var profileImage: String = "",

    @SerializedName("nickname")
    @Expose
    var nickname: String = ""
)