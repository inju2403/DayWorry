package com.inju.dayworry.worry.worryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WorryListViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<WorryListEvent>(uiContext) {

    private val worryListState = MutableLiveData<MutableList<Worry>>()
    val worryList: LiveData<MutableList<Worry>> get() = worryListState

    private var currentPage: Int = 0

    fun InitWorrys(tagName: String) = launch {
        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
        currentPage = 0
        worryListState.value = repo.getWorrys(tagName, currentPage++)
    }

    fun getWorrys(tagName: String) = launch {
        var newList = repo.getWorrys(tagName, currentPage++)
        //새로 불러온 아이템들을 붙임
        for(item in newList) worryListState.value?.add(item)
    }

    private val editWorryState = MutableLiveData<Long>()
    val editWorry: LiveData<Long> get() = editWorryState

    override fun handleEvent(event: WorryListEvent) {
        when (event) {
//            is WorryListEvent.OnStart -> getWorrys()
            is WorryListEvent.OnWorryItemClick -> editWorry(event.worryId)
        }
    }

    private fun editWorry(worryId: Long) {
        editWorryState.value = worryId
    }

}