package com.inju.dayworry.model.repository

import com.inju.dayworry.model.pojo.Worry

interface IDayWorryRepository {
    fun getWorrys(): MutableList<Worry>
    fun getWorryByIdWorry(id : String): Worry
    fun addOrUpdateWorry(worry : Worry)
    fun deleteWorry(id: String)
}