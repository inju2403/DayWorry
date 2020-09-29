package com.inju.dayworry.model.implementations

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworry.model.pojo.Contents
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

    override suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry> {
        return httpCall?.getWorrys(tagName, pageNum)!!
//        return mutableListOf<Worry>()
    }

    override suspend fun getWorryById(id : Long) : Worry {
        return httpCall?.getWorryById(id, token)!!
//        return Worry()
    }

    override suspend fun addOrUpdateWorry(id: Long, contents: Contents)  {
        httpCall?.addOrUpdateWorry(id, contents, token)
    }

    override suspend fun deleteWorry(id : Long) {
        httpCall?.deleteWorry(id, token)
    }

}