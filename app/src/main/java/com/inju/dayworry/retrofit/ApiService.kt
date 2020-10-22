package com.inju.dayworry.retrofit

import com.google.gson.JsonElement
import com.inju.dayworry.model.pojo.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
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

    //네이버 프로필 정보 조회
    @GET("v1/nid/me")
    suspend fun getNaverInfo(@Header("Authorization") Authorization: String): JsonElement

    //토큰 검증
    @POST("users/check")
    fun verifyJWT(@Header("jwt") jwt: String): Call<User_REQUEST_POJO>

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

    // 고민 상세 조회
    @GET("posts/{postId}")
    suspend fun getWorryById(@Path("postId") postId: Long): Worry

    //계정 삭제
    @DELETE("users/{userId}")
    fun deleteUser(@Path("userId") userId: Long): Call<Void>

    //고민 수정
    @POST("posts/")
    suspend fun addOrUpdateWorry(@Body contents: Contents): Response<Unit>

    //고민 사진 보내기
    @Multipart
    @POST("posts/image")
    suspend fun postImage(@Part file: MultipartBody.Part): IMAGE_RETURN_POJO

    //고민 삭제
    @DELETE("posts/{postId}")
    suspend fun deleteWorry(@Path("postId") postId: Long): Response<Unit>

    //댓글 리스트 받기
    @GET("comments/{postId}")
    suspend fun getComments(@Path("postId") poseId: Long,
                            @Query("pageNum") pageNum: Int,
                            @Query("userId") userId: Long): MutableList<Counsel>

    //댓글 작성
    @POST("comments")
    suspend fun addComment(@Body counselRequestPojo: COUNSEL_REQUEST_POJO): Response<Unit>

    //댓글 좋아요
    @PUT("comments/like")
    suspend fun likeComment(@Query("commentId") commentId: Long,
                            @Query("userId") userId: Long): Response<Unit>

    @GET("posts/home/story")
    suspend fun getStorys(): MutableList<Worry>

}