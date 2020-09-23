package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Contents(
    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("post_hashtag")
    @Expose
    var post_hashtag: String = "0"
)