package com.inju.dayworry.worry.worryDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.model.pojo.Contents
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import com.inju.dayworry.utils.Constants.TAG
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WorryDetailViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<WorryDetailEvent>(uiContext) {

    private val worryState = MutableLiveData<Worry>()
    val worry: LiveData<Worry> get() = worryState

    fun initWorry() {
        worryState.value = Worry()
    }

    fun getWorryById(id: Long) = launch {
        worryState.value = repo.getWorryById(id)
    }

    fun addOrUpdateWorry(userId: Long, tageName: String) = launch {
        var contents
                = Contents(
                    worry.value!!.content,
                    worry.value!!.postId,
                    worry.value!!.postImage,
                    tageName,
                    worry.value!!.title,
                    userId)

        repo.addOrUpdateWorry(contents)
    }

    fun deleteWorry(id: Long) = launch {
        repo.deleteWorry(id)
    }

    fun setWorryTitle(newTitle: String) {
        worryState.value!!.title = newTitle
    }

    fun setWorryContent(newContent: String) {
        worryState.value!!.content = newContent
    }

    override fun handleEvent(event: WorryDetailEvent) {
        TODO("Not yet implemented")
    }
}