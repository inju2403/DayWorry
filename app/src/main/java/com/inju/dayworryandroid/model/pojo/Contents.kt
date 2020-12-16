package com.inju.dayworryandroid.model.pojo

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
//    var postImage: MultipartBody.Part = MultipartBody.Part.createFormData("postImage", "fileName", "".toRequestBody("image/jpeg".toMediaTypeOrNull())),
    var postImage: String = "",

    @SerializedName("tagName")
    @Expose
    var tagName: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("userId")
    @Expose
    var userId: String = ""
)