package com.example.dayworry.model.repository

import com.example.dayworry.model.Worry

interface IWorryRepository {
    fun getWorrys(): MutableList<Worry>
}