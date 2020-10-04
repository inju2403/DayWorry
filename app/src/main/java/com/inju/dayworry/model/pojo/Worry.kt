package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Worry (

    @SerializedName("createdDate")
    @Expose
    var createdDate: String = "",

    @SerializedName("modifiedDate")
    @Expose
    var modifiedDate: String = "",

    @SerializedName("postId")
    @Expose
    var postId: Long = -1,

    @SerializedName("userId")
    @Expose
    var userId: Long = -1,

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("tagName")
    @Expose
    var tagName: String = "",

    @SerializedName("postImage")
    @Expose
    var postImage: String = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/HagoLogo.PNG",

    @SerializedName("hits")
    @Expose
    var hits: Long = 0,

    @SerializedName("postLikes")
    @Expose
    var postLikes: Int = 0

//    @SerializedName("userId")
//    @Expose
//    var userId: Long = 0
)