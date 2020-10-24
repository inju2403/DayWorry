package com.inju.dayworryandroid.model.repository

import com.inju.dayworryandroid.model.pojo.COUNSEL_REQUEST_POJO
import com.inju.dayworryandroid.model.pojo.Contents
import com.inju.dayworryandroid.model.pojo.Counsel
import com.inju.dayworryandroid.model.pojo.Worry
import okhttp3.MultipartBody

interface IDayWorryRepository {
    suspend fun getMainWorrys(): MutableList<Worry>
    suspend fun getMyWorrys(userId: Long, pageNum: Int): MutableList<Worry>
    suspend fun getHistory(userId: Long, pageNum: Int): MutableList<Worry>
    suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun keywordSearch(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun getWorryById(postId: Long): Worry
    suspend fun addOrUpdateWorry(contents: Contents)
    suspend fun postImage(file: MultipartBody.Part): String

    suspend fun deleteWorry(postId: Long)

    suspend fun getComments(postId: Long, pageNum: Int, userId: Long): MutableList<Counsel>
    suspend fun addComment(counselRequestPojo: COUNSEL_REQUEST_POJO)
    suspend fun likeComment(commentId: Long, userId: Long)
    suspend fun getStorys(): MutableList<Worry>
}