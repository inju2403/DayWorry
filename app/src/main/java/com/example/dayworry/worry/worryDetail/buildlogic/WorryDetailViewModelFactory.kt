package com.example.dayworry.worry.worryDetail.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dayworry.model.repository.IWorryRepository
import com.example.dayworry.worry.worryDetail.WorryDetailViewModel

class WorryDetailViewModelFactory(
    private val repo: IWorryRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorryDetailViewModel(repo) as T
    }
}