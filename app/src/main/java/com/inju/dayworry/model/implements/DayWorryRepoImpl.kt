package com.inju.dayworry.model.implements

import android.content.Context
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants.API_BASE_URL
import io.realm.Realm
import io.realm.Sort

class DayWorryRepoImpl(
    val httpCall: ApiService?
    = RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java),
    val context: Context
): IDayWorryRepository
{

    var list = mutableListOf<Worry>()

    override fun getWorrys(): MutableList<Worry> {
        return list
    }

    override fun getWorryById(id : Long) : Worry {
        return Worry()
    }

    override fun addOrUpdateWorry(worry : Worry)  {
    }

    override fun deleteWorry(id : Long) {

    }

}