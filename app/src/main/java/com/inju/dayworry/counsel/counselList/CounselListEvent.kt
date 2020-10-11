package com.inju.dayworry.counsel.counselList

sealed class CounselListEvent {
    data class OnCounselItemClick(val commentId: Long) : CounselListEvent()
    object OnNewCounselClick : CounselListEvent()
    object OnStart : CounselListEvent()
}