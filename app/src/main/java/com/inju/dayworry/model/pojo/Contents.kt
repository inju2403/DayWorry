package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Contents(
    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("postId")
    @Expose
    var postId: Long = -1,

    @SerializedName("postImage")
    @Expose
    var postImage: String = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/HagoLogo.PNG",

    @SerializedName("tagName")
    @Expose
    var tagName: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("userId")
    @Expose
    var userId: Long = -1
)