package com.inju.dayworry.retrofit

import com.inju.dayworry.model.pojo.Contents
import com.inju.dayworry.model.pojo.SOCIAL_LOGIN_RETURN_POJO
import com.inju.dayworry.model.pojo.Worry
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //카카오 로그인
    @POST("users/login/kakao")
    fun kakaoLogin(@Header("accessToken") accessToken: String): Call<SOCIAL_LOGIN_RETURN_POJO>

    //네이버 로그인
    @POST("users/login/naver")
    fun naverLogin(@Header("accessToken") accessToken: String): Call<SOCIAL_LOGIN_RETURN_POJO>

    //토큰 검증
    @POST("users/check")
    fun verifyJWT(@Header("jwt") jwt: String): Call<Void>

    @GET("posts/{tagName}") //고민 리스트 받기 -> 페이징 (현재페이지, 페이지당 아이템 갯수)
//    fun getWorrys(@Header("Authorization") Authorization: String): Single<MutableList<Worry>>

    suspend fun getWorrys(@Path("tagName") tagName: String,
                          @Query("pageNum") pageNum: Int): MutableList<Worry>

    @GET("path/{id}/") //고민 받기
//    fun getWorryById(@Path("id") id: Long, @Header("Authorization") Authorization: String): Single<Worry>
    suspend fun getWorryById(@Path("id") id: Long, @Header("Authorization") Authorization: String): Worry

    @PUT("path/{id}/") //고민 수정
    suspend fun addOrUpdateWorry(@Path("id") id: Long,
                                 @Body contents: Contents,
                                 @Header("Authorization") Authorization: String): Response<Unit>

    @DELETE("path/{id}/") //고민 삭제
//    fun deleteWorry(@Path("id") id: Long, @Header("Authorization") Authorization: String): Response<Unit>
    suspend fun deleteWorry(@Path("id") id: Long, @Header("Authorization") Authorization: String): Response<Unit>

}