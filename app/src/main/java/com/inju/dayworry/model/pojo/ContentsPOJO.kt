package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentsPOJO (
    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("content")
    @Expose
    var content: String = ""
)