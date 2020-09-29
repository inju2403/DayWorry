package com.inju.dayworry.counselList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CounselListViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<CounselListEvent>(uiContext) {

    private val counselListState = MutableLiveData<MutableList<Worry>>()
    val counselList: LiveData<MutableList<Worry>> get() = counselListState

    private var currentPage: Int = 0

    fun InitCounsels() = launch {
        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
//        currentPage = 0
//        counselListState.value = repo.getCounsels(tagName, currentPage++)
    }

    fun getCounsels() = launch {
//        var newList = repo.getCounsels(tagName, currentPage++)
//        //새로 불러온 아이템들을 붙임
//        for(item in newList) counselListState.value?.add(item)
    }


    override fun handleEvent(event: CounselListEvent) {
        TODO("Not yet implemented")
    }

}