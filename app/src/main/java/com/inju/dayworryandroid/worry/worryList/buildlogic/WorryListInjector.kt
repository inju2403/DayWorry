package com.inju.dayworryandroid.worry.worryList.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inju.dayworryandroid.model.implementations.DayWorryRepoImpl
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants.API_BASE_URL

class WorryListInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

    private val httpCall: ApiService?
            = RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java)


    private fun getWorryRepository(): IDayWorryRepository {
        return DayWorryRepoImpl(httpCall, app)
//        return DayWorryRepoImpl(app)
    }

    fun provideWorryListViewModelFactory(): WorryListViewModelFactory =
        WorryListViewModelFactory(
            getWorryRepository()
        )

}