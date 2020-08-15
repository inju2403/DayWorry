package com.example.dayworry.worry.worryDetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IDayWorryRepository

class WorryDetailViewModel(
    private val repo: IDayWorryRepository
): ViewModel() {

    var worry = Worry()
    val worryLiveData: MutableLiveData<Worry> by lazy {
        MutableLiveData<Worry>().apply {
            value = worry
        }
    }

    fun getWorryByIdWorry(id: String)  {
        worry = repo.getWorryByIdWorry(id)
        worryLiveData.value = worry
    }

    fun addOrUpdateWorry(context: Context)  {
        repo.addOrUpdateWorry(worry)
    }

    fun deleteWorry(id: String) {
        repo.deleteWorry(id)
    }
}