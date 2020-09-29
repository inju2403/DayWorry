package com.inju.dayworry.model.repository

import com.inju.dayworry.model.pojo.Contents
import com.inju.dayworry.model.pojo.Worry

interface IDayWorryRepository {
    suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun getWorryById(id: Long): Worry
    suspend fun addOrUpdateWorry(id: Long, contents: Contents)
    suspend fun deleteWorry(id: Long)
}