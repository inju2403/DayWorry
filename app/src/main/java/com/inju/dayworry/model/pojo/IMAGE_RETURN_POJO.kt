package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IMAGE_RETURN_POJO (
    @SerializedName("imgPath")
    @Expose
    var imgPath: String = ""
)