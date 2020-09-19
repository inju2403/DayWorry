package com.inju.dayworry.worry.worryList

sealed class WorryListEvent {
    data class OnWorryItemClick(val worryId: Long) : WorryListEvent()
    object OnNewWorryClick : WorryListEvent()
    object OnStart : WorryListEvent()
}