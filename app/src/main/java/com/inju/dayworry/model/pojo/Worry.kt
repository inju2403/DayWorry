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

    @SerializedName("created_date")
    @Expose
    var created_date: Date = Date(),

    @SerializedName("modified_date")
    @Expose
    var modified_date: Date = Date(),

    @SerializedName("content")
    @Expose
    var content: String = "",

    @SerializedName("expired")
    @Expose
    var expired: Int = 0,

    @SerializedName("hits")
    @Expose
    var hits: Long = 0,

    @SerializedName("post_likes")
    @Expose
    var post_likes: Int = 0,

    @SerializedName("post_hashtag")
    @Expose
    var post_hashtag: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("user_id")
    @Expose
    var user_id: Long = 0
)