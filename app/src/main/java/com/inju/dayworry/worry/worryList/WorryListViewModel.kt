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

    private var currentPage: Long = 0
    private var pageSize: Long = 15

    fun getWorrys() =launch {
        var newList = repo.getWorrys(currentPage++, pageSize)
        //새로 불러온 아이템들을 붙임
        for(item in newList) worryListState.value?.add(item)
    }

    private val editWorryState = MutableLiveData<Long>()
    val editWorry: LiveData<Long> get() = editWorryState

    override fun handleEvent(event: WorryListEvent) {
        when (event) {
            is WorryListEvent.OnStart -> getWorrys()
            is WorryListEvent.OnWorryItemClick -> editWorry(event.worryId)
        }
    }

    private fun editWorry(worryId: Long) {
        editWorryState.value = worryId
    }

}