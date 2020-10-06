package com.inju.dayworry.retrofit

import com.inju.dayworry.model.pojo.*
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

    //유저 정보 업데이트
    @PUT("users")
    fun updateProfile(@Body requestDto: User_REQUEST_POJO): Call<Void>

    //닉네임 중복 검사
    @GET("users/check/{nickname}")
    suspend fun nicknameRedundancyCheck(@Path("nickname") nickname: String): NICKNAME_REDUNDANCY_RETURN_POJO

    //메인 홈 탭에서 고민 리스트(3개) 받기
    @GET("posts/main")
    suspend fun getMainWorrys(): MutableList<Worry>

    //고민 리스트 받기 -> 페이징 (현재페이지, 페이지당 아이템 갯수)
    @GET("posts/home/{tagName}")
//    fun getWorrys(@Header("Authorization") Authorization: String): Single<MutableList<Worry>>
    suspend fun getWorrys(@Path("tagName") tagName: String,
                          @Query("pageNum") pageNum: Int): MutableList<Worry>

    //검색으로 고민 리스트 받기 -> 페이징 (현재페이지, 페이지당 아이템 갯수)
    @GET("posts/search/{keyword}")
    suspend fun keywordSearch(@Path("keyword") keyword: String,
                              @Query("pageNum") pageNum: Int): MutableList<Worry>

    // 현재 게시중인 내 고민 리스트 받기
    @GET("users/posts/{userId}")
    suspend fun getMyWorrys(@Path("userId") userId: Long,
                            @Query("pageNum") pageNum: Int): MutableList<Worry>

    // 지난 내 고민 리스트 받기
    @GET("users/history/{userId}")
    suspend fun getHistory(@Path("userId") userId: Long,
                           @Query("pageNum") pageNum: Int): MutableList<Worry>


    @GET("path/{id}/") //고민 받기
//    fun getWorryById(@Path("id") id: Long, @Header("Authorization") Authorization: String): Single<Worry>
    suspend fun getWorryById(@Path("id") id: Long, @Header("Authorization") Authorization: String): Worry

    @POST("posts/") //고민 수정
    suspend fun addOrUpdateWorry(@Body contents: Contents): Response<Unit>

    @DELETE("path/{id}/") //고민 삭제
//    fun deleteWorry(@Path("id") id: Long, @Header("Authorization") Authorization: String): Response<Unit>
    suspend fun deleteWorry(@Path("id") id: Long, @Header("Authorization") Authorization: String): Response<Unit>

}