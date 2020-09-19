package com.inju.dayworry.worry.worryDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository

class WorryDetailViewModel(
    private val repo: IDayWorryRepository
): ViewModel() {

//    var worry = Worry()
//    val worryLiveData: MutableLiveData<Worry> by lazy {
//        MutableLiveData<Worry>().apply {
//            value = worry
//        }
//    }

    private val worryState = MutableLiveData<Worry>()
    val worry: LiveData<Worry> get() = worryState

    fun getWorryById(id: Long)  {
        worryState.value = repo.getWorryById(id)
    }

    fun addOrUpdateWorry(context: Context)  {
        repo.addOrUpdateWorry(worry.value!!)
    }

    fun deleteWorry(id: Long) {
        repo.deleteWorry(id)
    }
}