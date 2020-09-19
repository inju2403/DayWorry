package com.inju.dayworry.worry.worryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.utils.BaseViewModel
import kotlin.coroutines.CoroutineContext

class WorryListViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<WorryListEvent>(uiContext) {

    private val worryListState = MutableLiveData<List<Worry>>()
    val worryList: LiveData<List<Worry>> get() = worryListState

    fun getWorrys() {
        worryListState.value = repo.getWorrys()
//        Log.d(Constants.TAG, "getWorrys :ok")
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