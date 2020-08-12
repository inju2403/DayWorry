package com.example.dayworry.worry.worryList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IWorryRepository
import com.example.dayworry.utils.Constants

class WorryListViewModel(
    private val repo: IWorryRepository
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