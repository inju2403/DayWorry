package com.inju.dayworry.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class KAKAO_RETURN_POJO(
    @SerializedName("jwt")
    @Expose
    var jwt: String = "",

    @SerializedName("status")
    @Expose
    var status: String = ""
)