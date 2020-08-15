package com.example.dayworry.counsel.counselDetail.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dayworry.counsel.counselDetail.CounselDetailViewModel
import com.example.dayworry.model.repository.IDayWorryRepository

class CounselDetailViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CounselDetailViewModel(repo) as T
    }
}