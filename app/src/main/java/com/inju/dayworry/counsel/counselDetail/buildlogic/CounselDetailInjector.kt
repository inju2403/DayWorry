package com.inju.dayworry.counsel.counselDetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inju.dayworry.counsel.counselList.buildlogic.CounselListViewModelFactory
import com.inju.dayworry.model.implementations.DayWorryRepoImpl
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants

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