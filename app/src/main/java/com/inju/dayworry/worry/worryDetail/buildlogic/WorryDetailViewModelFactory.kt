package com.inju.dayworry.worry.worryDetail.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.model.repository.IDayWorryRepository
import com.inju.dayworry.worry.worryDetail.WorryDetailViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class WorryDetailViewModelFactory(
    private val repo: IDayWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorryDetailViewModel(repo, Dispatchers.Main) as T
    }
}