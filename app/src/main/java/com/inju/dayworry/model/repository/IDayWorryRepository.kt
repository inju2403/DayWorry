package com.inju.dayworry.model.repository

import com.inju.dayworry.model.pojo.ContentsPOJO
import com.inju.dayworry.model.pojo.Worry

interface IDayWorryRepository {
    suspend fun getWorrys(currentPage: Long, pageSize: Long): MutableList<Worry>
    suspend fun getWorryById(id: Long): Worry
    suspend fun addOrUpdateWorry(contentsPOJO: ContentsPOJO, id: Long)
    suspend fun deleteWorry(id: Long)
}