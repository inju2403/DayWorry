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

    private var myWorryListitemCnt = 0
    fun getmyWorryListitemCnt() = myWorryListitemCnt

    private val mainWorryListState = MutableLiveData<MutableList<Worry>>()
    val mainWorryList: LiveData<MutableList<Worry>> get() = mainWorryListState

    private val worryListState = MutableLiveData<MutableList<Worry>>()
    val worryList: LiveData<MutableList<Worry>> get() = worryListState

    private val myWorryListState = MutableLiveData<MutableList<Worry>>()
    val myWorryList: LiveData<MutableList<Worry>> get() = myWorryListState

    private val myHistoryState = MutableLiveData<MutableList<Worry>>()
    val myHistory: LiveData<MutableList<Worry>> get() = myHistoryState

    private var usingTag: Boolean = false
    private var usingSearch: Boolean = false

    private var currentPage: Int = 0
    private var currentKeywordPage: Int = 0

    fun getMainWorrys() = launch {
        // 메인 홈 탭에서 추천 고민 3개를 가져옴
        mainWorryListState.value = repo.getMainWorrys()
    }

    //현재 게시중인 내 고민 리스트
    fun initMyWorrys(userId: Long) = launch {
        currentPage = 0
        myWorryListState.value = repo.getMyWorrys(userId, currentPage++)
        myWorryListitemCnt = myWorryList.value?.size!!
    }
    fun getMyWorrys(userId: Long) = launch {
        var newList = repo.getMyWorrys(userId, currentPage++)
        for(item in newList) myWorryListState.value?.add(item)
    }

    //지난 내 고민 리스트
    fun initHistory(userId: Long) = launch {
        currentPage = 0
        myHistoryState.value = repo.getHistory(userId, currentPage++)
    }
    fun getHistory(userId: Long) = launch {
        var newList = repo.getHistory(userId, currentPage++)
        for(item in newList) myHistoryState.value?.add(item)
    }

    fun InitWorrys(tagName: String) = launch {
        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름

        usingTag = true
        usingSearch = false
        currentPage = 0
        worryListState.value = repo.getWorrys(tagName, currentPage++)
    }

    fun getWorrys(tagName: String) = launch {
        var newList = repo.getWorrys(tagName, currentPage++)
//        Log.d(TAG,"newList: $newList")
        //새로 불러온 아이템들을 붙임
        for(item in newList) worryListState.value?.add(item)
    }

    fun initKeywordSearch(keyword: String) = launch {
        usingTag = false
        usingSearch = true
        currentKeywordPage = 0
        worryListState.value = repo.keywordSearch(keyword, currentKeywordPage++)
    }

    fun getKeywordSearch(keyword: String) = launch {
        var newList = repo.keywordSearch(keyword, currentPage++)
        //새로 불러온 아이템들을 붙임
        for(item in newList) worryListState.value?.add(item)
    }

    fun getWorrysState(): Boolean {
        //tag로 리스트를 받고 있으면 true, search로 받고 있으면 false 반환
        return usingTag
    }

    private val editWorryState = MutableLiveData<Long>()
    val editWorry: LiveData<Long> get() = editWorryState

    private val editMyWorryState = MutableLiveData<Long>()
    val editMyWorry: LiveData<Long> get() = editMyWorryState

    private val editHistoryState = MutableLiveData<Long>()
    val editHistory: LiveData<Long> get() = editHistoryState

    override fun handleEvent(event: WorryListEvent) {
        when (event) {
            is WorryListEvent.OnWorryItemClick -> editWorry(event.worryId)
            is WorryListEvent.OnMyWorryItemClick -> editMyWorry(event.worryId)
            is WorryListEvent.OnHistoryItemClick -> editHistory(event.worryId)
        }
    }

    private fun editWorry(worryId: Long) {
        editWorryState.value = worryId
    }

    private fun editMyWorry(worryId: Long) {
        editMyWorryState.value = worryId
    }

    private fun editHistory(worryId: Long) {
        editHistoryState.value = worryId
    }
}