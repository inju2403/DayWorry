package com.inju.dayworry.model.repository

import com.inju.dayworry.model.pojo.Worry

interface IDayWorryRepository {
    suspend fun getWorrys(currentPage: Long, pageSize: Long): MutableList<Worry>
    suspend fun getWorryById(id: Long): Worry
    suspend fun addOrUpdateWorry(title: String, content: String, tag: String, id: Long)
    suspend fun deleteWorry(id: Long)
}