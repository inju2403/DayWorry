package com.example.dayworry.retrofit

import com.example.dayworry.model.Worry
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    //고민 리스트 받기
    @GET("path")
    fun getWorrys(@Header("Authorization") Authorization: String): Single<Worry>
}