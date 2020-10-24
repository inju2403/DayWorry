package com.inju.dayworryandroid.counsel.counselDetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inju.dayworryandroid.model.implementations.DayWorryRepoImpl
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants

class CounselDetailInjector (application: Application): AndroidViewModel(application) {

    val app: Application = application

    private val httpCall: ApiService? =
        RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
            ApiService::class.java
        )

    private fun getCounselDetailRepository(): IDayWorryRepository {
        return DayWorryRepoImpl(httpCall, app)
    }

    fun provideCounselDetailViewModelFactory(): CounselDetailViewModelFactory =
        CounselDetailViewModelFactory(
            getCounselDetailRepository()
        )
}