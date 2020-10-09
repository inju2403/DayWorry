package com.inju.dayworry.counselList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.model.pojo.Counsel
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import com.inju.dayworry.utils.Constants.TAG
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CounselListViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<CounselListEvent>(uiContext) {

    private val counselListState = MutableLiveData<MutableList<Counsel>>()
    val counselList: LiveData<MutableList<Counsel>> get() = counselListState

    private var currentPage: Int = 0

    fun InitCounsels(postId: Long) = launch {
        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
        currentPage = 0
        counselListState.value = repo.getComments(postId, currentPage++)
        Log.d(TAG, "댓글: ${counselListState.value}")
    }

    fun getCounsels(postId: Long) = launch {
        var newList = repo.getComments(postId, currentPage++)
        //새로 불러온 아이템들을 붙임
        for(item in newList) counselListState.value?.add(item)
    }


    override fun handleEvent(event: CounselListEvent) {
        TODO("Not yet implemented")
    }

}