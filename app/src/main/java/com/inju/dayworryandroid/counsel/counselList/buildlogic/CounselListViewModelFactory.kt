package com.inju.dayworryandroid.counsel.counselList.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworryandroid.counsel.counselList.CounselListViewModel
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import kotlinx.coroutines.Dispatchers

class CounselListViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CounselListViewModel(
            repo,
            Dispatchers.Main
        ) as T
    }
}