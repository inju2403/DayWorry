package com.inju.dayworryandroid.counsel.counselList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworryandroid.model.pojo.Counsel
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.utils.BaseViewModel
import com.inju.dayworryandroid.utils.Constants.TAG
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CounselListViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<CounselListEvent>(uiContext) {

    private val counselListState = MutableLiveData<MutableList<Counsel>>()
    val counselList: LiveData<MutableList<Counsel>> get() = counselListState

    private var currentPage: Int = 0

    private var curPostId: Long = 0
    private var curUserId: String = ""

    fun InitCounsels(postId: Long, userId: String) = launch {
        curPostId = postId
        curUserId = userId
        // 댓글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
        currentPage = 0
        Log.d(TAG, "userId: $userId")
        counselListState.value = repo.getComments(postId, currentPage++, userId)
        Log.d(TAG, "댓글: ${counselListState.value}")
    }

    fun getCounsels(postId: Long, userId: String) = launch {
        var newList = repo.getComments(postId, currentPage++, userId)
        //새로 불러온 아이템들을 붙임
        for(item in newList) counselListState.value?.add(item)
    }

    override fun handleEvent(event: CounselListEvent) {
        when(event) {
            is CounselListEvent.OnCounselItemClick -> sendLike(event.commentId)
        }
    }

    private fun sendLike(commentId: Long) = launch {
        repo.likeComment(commentId, curUserId)
        InitCounsels(curPostId, curUserId)
    }

}