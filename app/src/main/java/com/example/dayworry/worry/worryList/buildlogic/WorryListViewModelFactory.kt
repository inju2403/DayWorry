package com.example.dayworry.worry.worryList.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dayworry.model.repository.IDayWorryRepository
import com.example.dayworry.worry.worryList.WorryListViewModel

class WorryListViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorryListViewModel(repo) as T
    }
}