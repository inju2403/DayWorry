package com.inju.dayworry.worry.worryList

sealed class WorryListEvent {
    data class OnWorryItemClick(val worryId: Long) : WorryListEvent()
    data class OnMyWorryItemClick(val worryId: Long) : WorryListEvent()
    data class OnHistoryItemClick(val worryId: Long) : WorryListEvent()
    data class OnStoryItemClick(val worryId: Long) : WorryListEvent()
    data class OnSearchItemClick(val worryId: Long) : WorryListEvent()
    object OnNewWorryClick : WorryListEvent()
    object OnStart : WorryListEvent()
}