package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Counsel (
    @SerializedName("comment_id")
    @Expose
    var comment_id: Long = 0,

    @SerializedName("user_id")
    @Expose
    var user_id: Long = 0,

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("comment_likes")
    @Expose
    var comment_likes: Long = 0,
)