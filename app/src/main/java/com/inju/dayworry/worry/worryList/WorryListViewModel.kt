package com.inju.dayworry.worry.worryList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inju.dayworry.model.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository

class WorryListViewModel(
    private val repo: IDayWorryRepository
): ViewModel() {

    var worrys: MutableList<Worry> = mutableListOf()
    val worryListLiveData: MutableLiveData<MutableList<Worry>> by lazy {
        MutableLiveData<MutableList<Worry>>().apply {
            value = worrys
        }
    }

    fun getWorrys() {
        worrys = repo.getWorrys()
        worryListLiveData.value = worrys
//        Log.d(Constants.TAG, "getWorrys :ok")
    }

}