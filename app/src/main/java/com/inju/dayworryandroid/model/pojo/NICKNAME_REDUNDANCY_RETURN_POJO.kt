package com.inju.dayworryandroid.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NICKNAME_REDUNDANCY_RETURN_POJO(
    @SerializedName("flag")
    @Expose
    var flag: Boolean = false
)