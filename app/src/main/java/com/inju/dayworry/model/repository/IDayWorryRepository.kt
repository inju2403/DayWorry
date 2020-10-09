package com.inju.dayworry.model.repository

import com.inju.dayworry.model.pojo.Contents
import com.inju.dayworry.model.pojo.Counsel
import com.inju.dayworry.model.pojo.Worry

interface IDayWorryRepository {
    suspend fun getMainWorrys(): MutableList<Worry>
    suspend fun getMyWorrys(userId: Long, pageNum: Int): MutableList<Worry>
    suspend fun getHistory(userId: Long, pageNum: Int): MutableList<Worry>
    suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun keywordSearch(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun getWorryById(postId: Long): Worry
    suspend fun addOrUpdateWorry(contents: Contents)
    suspend fun deleteWorry(id: Long)

    suspend fun getComments(postId: Long, pageNum: Int): MutableList<Counsel>
}