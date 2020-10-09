package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Counsel (
    @SerializedName("commentId")
    @Expose
    var commentId: Long = 0,

    @SerializedName("createdDate")
    @Expose
    var createdDate: String = "",

    @SerializedName("modifiedDate")
    @Expose
    var modifiedDate: String = "",

    @SerializedName("commentLikes")
    @Expose
    var commentLikes: Long = 0,

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("userId")
    @Expose
    var userId: Long = 0,

    @SerializedName("profileImage")
    @Expose
    var profileImage: String = "",

    @SerializedName("nickname")
    @Expose
    var nickname: String = ""
)