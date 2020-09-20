package com.inju.dayworry.retrofit

import com.inju.dayworry.model.pojo.ContentsPOJO
import com.inju.dayworry.model.pojo.Worry
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("path/") //고민 리스트 받기
    fun getWorrys(@Header("Authorization") Authorization: String): Single<Worry>

    @GET("path/{id}/") //고민 받기
    fun getWorryById(@Path("id") id: Long, @Header("Authorization") Authorization: String): Single<Worry>

    @PUT("path/{id}/") //고민 수정
    fun addOrUpdateWorry(@Path("id") id: Long,
                    @Body contentsPOJO: ContentsPOJO,
                    @Header("Authorization") Authorization: String): Response<Unit>

    @DELETE("path/{id}/") //고민 삭제
    fun deleteWorry(@Path("id") id: Long, @Header("Authorization") Authorization: String): Response<Unit>

}