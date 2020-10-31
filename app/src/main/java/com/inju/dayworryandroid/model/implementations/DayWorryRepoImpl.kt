package com.inju.dayworryandroid.model.implementations

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworryandroid.model.pojo.COUNSEL_REQUEST_POJO
import com.inju.dayworryandroid.model.pojo.Contents
import com.inju.dayworryandroid.model.pojo.Counsel
import com.inju.dayworryandroid.model.pojo.Worry
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants.API_BASE_URL
import com.inju.dayworryandroid.utils.Constants.PREFERENCE
import okhttp3.MultipartBody

class DayWorryRepoImpl(
    val httpCall: ApiService?
    = RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java),
    val context: Context
): IDayWorryRepository
{

    val pref = context.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    val str = pref.getString("token", "").toString()
    val token = "JWT $str"

    override suspend fun getMainWorrys(userId: Long): MutableList<Worry> {
        return httpCall?.getMainWorrys(userId)!!
    }

    override suspend fun getMyWorrys(userId: Long, pageNum: Int): MutableList<Worry> {
        return httpCall?.getMyWorrys(userId, pageNum)!!
    }

    override suspend fun getHistory(userId: Long, pageNum: Int): MutableList<Worry> {
        return httpCall?.getHistory(userId, pageNum)!!
    }

    override suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry> {
        return httpCall?.getWorrys(tagName, pageNum)!!
    }

    override suspend fun keywordSearch(tagName: String, pageNum: Int): MutableList<Worry> {
        return httpCall?.keywordSearch(tagName, pageNum)!!
    }

    override suspend fun getWorryById(postId : Long) : Worry {
        return httpCall?.getWorryById(postId)!!
    }

    override suspend fun addOrUpdateWorry(contents: Contents)  {
        httpCall?.addOrUpdateWorry(contents)
    }

    override suspend fun postImage(file: MultipartBody.Part): String {
        return httpCall?.postImage(file)!!.imgPath
    }

    override suspend fun deleteWorry(postId : Long) {
        httpCall?.deleteWorry(postId)
    }

    override suspend fun getComments(postId: Long, pageNum: Int, userId: Long): MutableList<Counsel> {
        return httpCall?.getComments(postId, pageNum, userId)!!
    }

    override suspend fun addComment(counselRequestPojo: COUNSEL_REQUEST_POJO) {
        httpCall?.addComment(counselRequestPojo)
    }

    override suspend fun likeComment(commentId: Long, userId: Long) {
        httpCall?.likeComment(commentId, userId)
    }

    override suspend fun getStorys(): MutableList<Worry> {
        return httpCall?.getStorys()!!
    }


}