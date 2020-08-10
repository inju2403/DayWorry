package com.example.dayworry.model.implements

import android.content.Context
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IWorryRepository
import com.example.dayworry.retrofit.ApiService
import com.example.dayworry.retrofit.RetrofitClient
import com.example.dayworry.utils.Constants

class WorryRepoImpl(
    val httpCall: ApiService?
    = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(ApiService::class.java),
    val context: Context
): IWorryRepository
{
    var tmpWorrys = mutableListOf<Worry>()

    override fun getWorrys(): MutableList<Worry> {
        return tmpWorrys
    }

}