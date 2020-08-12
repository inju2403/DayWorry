package com.example.dayworry.worry.worryList.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dayworry.model.implements.WorryRepoImpl
import com.example.dayworry.model.repository.IWorryRepository
import io.realm.Realm

class WorryListInjector(application: Application): AndroidViewModel(application) {

    val app: Application = application

//    private val httpCall: ApiService?
//            = com.example.dayworry.retrofit.RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java)


    private fun getWorryRepository(): IWorryRepository {
//        return WorryRepoImpl(httpCall, app)
        return WorryRepoImpl(app)
    }

    fun provideWorryListViewModelFactory(): WorryListViewModelFactory =
        WorryListViewModelFactory(
            getWorryRepository()
        )

}