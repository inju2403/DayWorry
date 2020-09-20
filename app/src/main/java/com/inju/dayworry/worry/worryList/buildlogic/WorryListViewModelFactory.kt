package com.inju.dayworry.worry.worryList.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.worry.worryList.WorryListViewModel
import kotlinx.coroutines.Dispatchers

class WorryListViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorryListViewModel(repo, Dispatchers.Main) as T
    }
}