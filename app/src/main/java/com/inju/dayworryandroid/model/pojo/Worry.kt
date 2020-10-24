package com.inju.dayworryandroid.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    var postImage: String = "",

    @SerializedName("hits")
    @Expose
    var hits: Long = 0,

    @SerializedName("commentNum")
    @Expose
    var commentNum: Int = 0,

    @SerializedName("userNickname")
    @Expose
    var userNickname: String = "",

    @SerializedName("userProfileImage")
    @Expose
    var userProfileImage: String = ""
)