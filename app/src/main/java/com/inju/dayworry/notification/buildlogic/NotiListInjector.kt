package com.inju.dayworry.notification.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inju.dayworry.counsel.counselList.buildlogic.CounselListViewModelFactory
import com.inju.dayworry.model.implements.DayWorryRepoImpl
import com.inju.dayworry.model.repository.IDayWorryRepository

class NotiListInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

//    private val httpCall: ApiService? =
//        com.inju.dayworry.retrofit.RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
//            ApiService::class.java
//        )

    private fun getNotiListRepository(): IDayWorryRepository {
//        return WorryRepoImpl(httpCall, app)
        return DayWorryRepoImpl(app)
    }

    fun provideNotiListViewModelFactory(): NotiListViewModelFactory =
        NotiListViewModelFactory(
            getNotiListRepository()
        )
}