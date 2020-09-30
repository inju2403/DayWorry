package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

data class Worry (
    @SerializedName("postId")
    @Expose
    var postId: Long = -1,

    @SerializedName("createdDate")
    @Expose
    var createdDate: Date = Date(),

    @SerializedName("modifiedDate")
    @Expose
    var modifiedDate: Date = Date(),

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("hits")
    @Expose
    var hits: Long = -1,

    @SerializedName("postLikes")
    @Expose
    var postLikes: Int = 0,

    @SerializedName("tagName")
    @Expose
    var tagName: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("userId")
    @Expose
    var userId: Long = 0,

    @SerializedName("user_id")
    @Expose
    var comments: MutableList<Counsel> = mutableListOf()
)