package com.inju.dayworry.worry.worryDetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inju.dayworry.model.implementations.DayWorryRepoImpl
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants.API_BASE_URL

class WorryDetailInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

    private val httpCall: ApiService? =
        RetrofitClient.getClient(API_BASE_URL)!!.create(
            ApiService::class.java
        )

    private fun getWorryRepository(): IDayWorryRepository {
        return DayWorryRepoImpl(httpCall, app)
    }

    fun provideWorryDetailViewModelFactory(): WorryDetailViewModelFactory =
        WorryDetailViewModelFactory(
            getWorryRepository()
        )
}