package com.inju.dayworry.counsel.counselDetail.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.counsel.counselDetail.CounselDetailViewModel
import com.inju.dayworry.model.repository.IDayWorryRepository
import kotlinx.coroutines.Dispatchers

class CounselDetailViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CounselDetailViewModel(repo, Dispatchers.Main) as T
    }
}