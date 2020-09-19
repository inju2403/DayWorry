package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

data class Worry (
    @SerializedName("post_id")
    @Expose
    var post_id: Long = 0,

    @SerializedName("user_id")
    @Expose
    var user_id: Long = 0,

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("post_hashtag")
    @Expose
    var post_hashtag: String = "",

    @SerializedName("comments")
    @Expose
    var comments: MutableList<Counsel> = mutableListOf<Counsel>(),

    @SerializedName("hits")
    @Expose
    var hits: Long = 0,

    @SerializedName("likes")
    @Expose
    var likes: Long = 0,

    @SerializedName("expired")
    @Expose
    var expired: Boolean = false,
)