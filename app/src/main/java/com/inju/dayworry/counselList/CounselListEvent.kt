package com.inju.dayworry.counselList

sealed class CounselListEvent {
    data class OnCounselItemClick(val worryId: Long) : CounselListEvent()
    object OnNewCounselClick : CounselListEvent()
    object OnStart : CounselListEvent()
}