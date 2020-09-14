package com.inju.dayworry.counsel.counselList.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.counsel.counselList.CounselListViewModel
import com.inju.dayworry.model.repository.IDayWorryRepository

class CounselListViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CounselListViewModel(repo) as T
    }
}