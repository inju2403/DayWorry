package com.example.dayworry.worry.worryList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IWorryRepository

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
    }
}