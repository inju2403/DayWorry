package com.inju.dayworry.counsel.counselDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.counsel.counselList.CounselListEvent
import com.inju.dayworry.model.pojo.COUNSEL_REQUEST_POJO
import com.inju.dayworry.model.pojo.Contents
import com.inju.dayworry.model.pojo.Counsel
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CounselDetailViewModel (
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<CounselListEvent>(uiContext) {

    private val counselState = MutableLiveData<COUNSEL_REQUEST_POJO>()
    val counsel: LiveData<COUNSEL_REQUEST_POJO> get() = counselState

    fun initCounsel() {
        counselState.value = COUNSEL_REQUEST_POJO()
    }

    fun addCounsel(content: String, userId: Long, postId: Long, profileImage: String, nickname: String) = launch {
        var counsel = COUNSEL_REQUEST_POJO(content, userId, postId, profileImage, nickname)
        repo.addComment(counsel)
    }

    fun setCounselContent(newContent: String) {
        counselState.value!!.content = newContent
    }

    override fun handleEvent(event: CounselListEvent) {
        TODO("Not yet implemented")
    }
}