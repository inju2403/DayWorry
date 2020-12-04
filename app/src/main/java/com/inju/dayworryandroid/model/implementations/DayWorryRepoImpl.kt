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
        try {
            return httpCall?.getMainWorrys(userId)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getMyWorrys(userId: Long, pageNum: Int): MutableList<Worry> {
        try {
            return httpCall?.getMyWorrys(userId, pageNum)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getHistory(userId: Long, pageNum: Int): MutableList<Worry> {
        try {
            return httpCall?.getHistory(userId, pageNum)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry> {
        try {
            return httpCall?.getWorrys(tagName, pageNum)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun keywordSearch(tagName: String, pageNum: Int): MutableList<Worry> {
        try {
            return httpCall?.keywordSearch(tagName, pageNum)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getWorryById(postId : Long) : Worry {
        try {
            return httpCall?.getWorryById(postId)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun addOrUpdateWorry(contents: Contents)  {
        try {
            httpCall?.addOrUpdateWorry(contents)
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun postImage(file: MultipartBody.Part): String {
        try {
            return httpCall?.postImage(file)!!.imgPath
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun deleteWorry(postId : Long) {
        try {
            httpCall?.deleteWorry(postId)
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getComments(postId: Long, pageNum: Int, userId: Long): MutableList<Counsel> {
        try {
            return httpCall?.getComments(postId, pageNum, userId)!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun addComment(counselRequestPojo: COUNSEL_REQUEST_POJO) {
        try {
            httpCall?.addComment(counselRequestPojo)
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun likeComment(commentId: Long, userId: Long) {
        try {
            httpCall?.likeComment(commentId, userId)
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getStorys(): MutableList<Worry> {
        try {
            return httpCall?.getStorys()!!
        }catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


}