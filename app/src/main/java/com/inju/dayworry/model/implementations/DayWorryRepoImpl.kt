package com.inju.dayworry.model.implementations

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants.API_BASE_URL
import com.inju.dayworry.utils.Constants.PREFERENCE

class DayWorryRepoImpl(
    val httpCall: ApiService?
    = RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java),
    val context: Context
): IDayWorryRepository
{

    val pref = context.getSharedPreferences(PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    val str = pref.getString("token", "").toString()
    val token = "JWT $str"

    override suspend fun getWorrys(post_hashtag: String, currentPage: Long, pageSize: Long, sort: String): MutableList<Worry> {
        return httpCall?.getWorrys(post_hashtag, currentPage, pageSize, sort)!!
//        return mutableListOf<Worry>()
    }

    override suspend fun getWorryById(id : Long) : Worry {
        return httpCall?.getWorryById(id, token)!!
//        return Worry()
    }

    override suspend fun addOrUpdateWorry(title: String, content: String, tag: String, id: Long)  {
        httpCall?.addOrUpdateWorry(id, title, content, tag, token)
    }

    override suspend fun deleteWorry(id : Long) {
        httpCall?.deleteWorry(id, token)
    }

}