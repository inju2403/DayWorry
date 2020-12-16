package com.inju.dayworryandroid.worry.worryDetail

sealed class WorryDetailEvent {
    data class OnDoneClick(val contents: String) : WorryDetailEvent()
    object OnDeleteClick : WorryDetailEvent()
    object OnDeleteConfirmed : WorryDetailEvent()
    data class OnStart(val id: String) : WorryDetailEvent()
}