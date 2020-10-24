package com.inju.dayworryandroid.worry.worryDetail.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworryandroid.model.repository.IDayWorryRepository
import com.inju.dayworryandroid.worry.worryDetail.WorryDetailViewModel
import kotlinx.coroutines.Dispatchers

class WorryDetailViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorryDetailViewModel(repo, Dispatchers.Main) as T
    }
}