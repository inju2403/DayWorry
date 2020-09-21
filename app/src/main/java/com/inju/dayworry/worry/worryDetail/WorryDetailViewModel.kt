package com.inju.dayworry.worry.worryDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WorryDetailViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<WorryDetailEvent>(uiContext) {

    private val worryState = MutableLiveData<Worry>()
    val worry: LiveData<Worry> get() = worryState

    fun getWorryById(id: Long) = launch {
        worryState.value = repo.getWorryById(id)
    }

    fun addOrUpdateWorry(context: Context, tag: String) = launch {
        repo.addOrUpdateWorry(worry.value!!.title, worry.value!!.content, tag, worry.value!!.post_id)
    }

    fun deleteWorry(id: Long) = launch {
        repo.deleteWorry(id)
    }

    fun setWorryTitle(newTitle: String) {
        worryState.value?.title = newTitle
    }

    fun setWorryContent(newContent: String) {
        worryState.value?.content = newContent
    }

    override fun handleEvent(event: WorryDetailEvent) {
        TODO("Not yet implemented")
    }
}