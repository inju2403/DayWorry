package com.example.dayworry.worry.worryDetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dayworry.model.implements.WorryRepoImpl
import com.example.dayworry.model.repository.IWorryRepository
import io.realm.Realm

class WorryDetailInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

//    private val httpCall: ApiService? =
//        com.example.dayworry.retrofit.RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
//            ApiService::class.java
//        )

    private fun getWorryRepository(): IWorryRepository {
//        return WorryRepoImpl(httpCall, app)
        return WorryRepoImpl(app)
    }

    fun provideWorryDetailViewModelFactory(): WorryDetailViewModelFactory =
        WorryDetailViewModelFactory(
            getWorryRepository()
        )
}