package com.inju.dayworryandroid.worry.worryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inju.dayworryandroid.model.pojo.Worry
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.utils.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WorryListViewModel(
    private val repo: IDayWorryRepository,
    uiContext: CoroutineContext
): BaseViewModel<WorryListEvent>(uiContext) {

    private var myWorryListitemCnt = 0
    fun getmyWorryListitemCnt() = myWorryListitemCnt

    private var mySearchListitemCnt = 0
    fun getmySearchListitemCnt() = mySearchListitemCnt

    private val mainWorryListState = MutableLiveData<MutableList<Worry>>()
    val mainWorryList: LiveData<MutableList<Worry>> get() = mainWorryListState

    private val worryListState = MutableLiveData<MutableList<Worry>>()
    val worryList: LiveData<MutableList<Worry>> get() = worryListState

    private val myWorryListState = MutableLiveData<MutableList<Worry>>()
    val myWorryList: LiveData<MutableList<Worry>> get() = myWorryListState

    private val myHistoryState = MutableLiveData<MutableList<Worry>>()
    val myHistory: LiveData<MutableList<Worry>> get() = myHistoryState

    private val storyState = MutableLiveData<MutableList<Worry>>()
    val story: LiveData<MutableList<Worry>> get() = storyState

    private val searchState = MutableLiveData<MutableList<Worry>>()
    val search: LiveData<MutableList<Worry>> get() = searchState

    private var currentWorryPage: Int = 0
    private var currentMyWorrydPage: Int = 0
    private var currentHistoryPage: Int = 0
    private var currentSearchPage: Int = 0
    private var currentStoryPage: Int = 0 // 이후에 스토리를 페이징으로 변경할 때를 위해서 미리 할당

    fun getStorys() = launch {
        storyState.value = repo.getStorys()
    }

    fun getMainWorrys(userId: String) = launch {
        // 메인 홈 탭에서 추천 고민 3개를 가져옴
        mainWorryListState.value = repo.getMainWorrys(userId)
    }

    //현재 게시중인 내 고민 리스트
    fun initMyWorrys(userId: String) = launch {
        currentMyWorrydPage = 0
        myWorryListState.value = repo.getMyWorrys(userId, currentMyWorrydPage++)
        myWorryListitemCnt = myWorryList.value?.size!!
    }
    fun getMyWorrys(userId: String) = launch {
        var newList = repo.getMyWorrys(userId, currentMyWorrydPage++)
        for(item in newList) myWorryListState.value?.add(item)
    }

    //지난 내 고민 리스트
    fun initHistory(userId: String) = launch {
        currentHistoryPage = 0
        myHistoryState.value = repo.getHistory(userId, currentHistoryPage++)
    }
    fun getHistory(userId: String) = launch {
        var newList = repo.getHistory(userId, currentHistoryPage++)
        for(item in newList) myHistoryState.value?.add(item)
    }

    // 돋보기 고민 리스트 탭
    fun InitWorrys(tagName: String) = launch {
        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
        currentWorryPage = 0
        worryListState.value = repo.getWorrys(tagName, currentWorryPage++)
    }

    fun getWorrys(tagName: String) = launch {
        var newList = repo.getWorrys(tagName, currentWorryPage++)
        //새로 불러온 아이템들을 붙임
        for(item in newList) worryListState.value?.add(item)
    }

    // 검색으로 고민 리스트 불러오기
    fun initKeywordSearch(keyword: String) = launch {
        currentSearchPage = 0
        searchState.value = repo.keywordSearch(keyword, currentSearchPage++)
        mySearchListitemCnt = search.value?.size!!
    }

    fun getKeywordSearch(keyword: String) = launch {
        var newList = repo.keywordSearch(keyword, currentSearchPage++)
        //새로 불러온 아이템들을 붙임
        for(item in newList) searchState.value?.add(item)
    }

    private val editWorryState = MutableLiveData<Long>()
    val editWorry: LiveData<Long> get() = editWorryState

    private val editMyWorryState = MutableLiveData<Long>()
    val editMyWorry: LiveData<Long> get() = editMyWorryState

    private val editHistoryState = MutableLiveData<Long>()
    val editHistory: LiveData<Long> get() = editHistoryState

    private val editStoryState = MutableLiveData<Long>()
    val editStory: LiveData<Long> get() = editStoryState

    private val editSearchState = MutableLiveData<Long>()
    val editSearch: LiveData<Long> get() = editSearchState

    override fun handleEvent(event: WorryListEvent) {
        when (event) {
            is WorryListEvent.OnWorryItemClick -> editWorry(event.worryId)
            is WorryListEvent.OnMyWorryItemClick -> editMyWorry(event.worryId)
            is WorryListEvent.OnHistoryItemClick -> editHistory(event.worryId)
            is WorryListEvent.OnStoryItemClick -> editStory(event.worryId)
            is WorryListEvent.OnSearchItemClick -> editSearh(event.worryId)
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

    private fun editStory(worryId: Long) {
        editStoryState.value = worryId
    }

    private fun editSearh(worryId: Long) {
        editSearchState.value = worryId
    }
}