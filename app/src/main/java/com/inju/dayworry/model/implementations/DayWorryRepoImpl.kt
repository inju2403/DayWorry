package com.inju.dayworry.model.implementations

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworry.model.pojo.ContentsPOJO
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

    override suspend fun getWorrys(currentPage: Long, pageSize: Long): MutableList<Worry> {
        return httpCall?.getWorrys(token, currentPage, pageSize)!!
//        return mutableListOf<Worry>()
    }

    override suspend fun getWorryById(id : Long) : Worry {
        return httpCall?.getWorryById(id, token)!!
//        return Worry()
    }

    override suspend fun addOrUpdateWorry(contentsPOJO: ContentsPOJO, id: Long)  {
        httpCall?.addOrUpdateWorry(id, contentsPOJO, token)
    }

    override suspend fun deleteWorry(id : Long) {
        httpCall?.deleteWorry(id, token)
    }

}