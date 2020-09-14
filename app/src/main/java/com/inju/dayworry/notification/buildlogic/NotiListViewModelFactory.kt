package com.inju.dayworry.notification.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.notification.notiList.NotiListViewModel

class NotiListViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotiListViewModel(repo) as T
    }
}