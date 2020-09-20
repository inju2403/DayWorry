package com.inju.dayworry.worry.worryDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inju.dayworry.model.pojo.ContentsPOJO
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
        val contentsPOJO = ContentsPOJO(worry.value!!.title, worry.value!!.content)
        repo.addOrUpdateWorry(contentsPOJO, worry.value!!.post_id)
    }

    fun deleteWorry(id: Long) {
        repo.deleteWorry(id)
    }
}