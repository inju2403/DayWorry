package com.inju.dayworryandroid.worry.worryDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworryandroid.model.pojo.Contents
import com.inju.dayworryandroid.model.pojo.Worry
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.utils.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

    fun getWorryById(postId: Long) = launch {
        worryState.value = repo.getWorryById(postId)
    }

    fun addOrUpdateWorry(userId: String, tageName: String) = launch {
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

    fun postImage(file: MultipartBody.Part) = launch {
        Log.d("로그그", "$file")
        worry.value!!.postImage = repo.postImage(file)
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