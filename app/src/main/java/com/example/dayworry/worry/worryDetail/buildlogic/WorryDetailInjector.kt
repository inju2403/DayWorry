package com.example.dayworry.worry.worryDetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dayworry.model.implements.WorryRepoImpl
import com.example.dayworry.model.repository.IWorryRepository
import com.example.dayworry.retrofit.ApiService
import com.example.dayworry.utils.Constants
import com.example.dayworry.worry.worryList.buildlogic.WorryListViewModelFactory

class WorryDetailInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

    private val httpCall: ApiService? =
        com.example.dayworry.retrofit.RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
            ApiService::class.java
        )

    private fun getDiaryRepository(): IWorryRepository {
        return WorryRepoImpl(httpCall, app)
    }

    fun provideDiaryListViewModelFactory(): WorryListViewModelFactory =
        WorryListViewModelFactory(
            getDiaryRepository()
        )
}