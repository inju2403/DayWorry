package com.example.dayworry.worry.worryDetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IWorryRepository

class WorryDetailViewModel(
    private val repo: IWorryRepository
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

    fun addOrUpdateDiary(worry: Worry)  {
        repo.addOrUpdateWorry(worry)
    }

    fun deleteDiary(id: String) {
        repo.deleteWorry(id)
    }
}