package com.example.dayworry.worry.worryDetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dayworry.model.implements.DayWorryRepoImpl
import com.example.dayworry.model.repository.IDayWorryRepository

class WorryDetailInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

//    private val httpCall: ApiService? =
//        com.example.dayworry.retrofit.RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
//            ApiService::class.java
//        )

    private fun getWorryRepository(): IDayWorryRepository {
//        return WorryRepoImpl(httpCall, app)
        return DayWorryRepoImpl(app)
    }

    fun provideWorryDetailViewModelFactory(): WorryDetailViewModelFactory =
        WorryDetailViewModelFactory(
            getWorryRepository()
        )
}