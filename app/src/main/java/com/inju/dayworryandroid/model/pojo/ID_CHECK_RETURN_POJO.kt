package com.inju.dayworryandroid.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ID_CHECK_RETURN_POJO (
    @SerializedName("flag")
    @Expose
    var flag: Boolean = false
)