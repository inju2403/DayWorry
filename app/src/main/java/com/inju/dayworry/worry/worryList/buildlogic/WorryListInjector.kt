package com.inju.dayworry.worry.worryList.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inju.dayworry.model.implements.DayWorryRepoImpl
import com.inju.dayworry.model.repository.IDayWorryRepository

class WorryListInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

//    private val httpCall: ApiService?
//            = com.inju.dayworry.retrofit.RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java)


    private fun getWorryRepository(): IDayWorryRepository {
//        return WorryRepoImpl(httpCall, app)
        return DayWorryRepoImpl(app)
    }

    fun provideWorryListViewModelFactory(): WorryListViewModelFactory =
        WorryListViewModelFactory(
            getWorryRepository()
        )

}