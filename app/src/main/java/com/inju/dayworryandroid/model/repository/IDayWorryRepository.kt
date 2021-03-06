package com.inju.dayworryandroid.model.repository

import com.inju.dayworryandroid.model.pojo.COUNSEL_REQUEST_POJO
import com.inju.dayworryandroid.model.pojo.Contents
import com.inju.dayworryandroid.model.pojo.Counsel
import com.inju.dayworryandroid.model.pojo.Worry
import okhttp3.MultipartBody

interface IDayWorryRepository {
    suspend fun getMainWorrys(userId: String): MutableList<Worry>
    suspend fun getMyWorrys(userId: String, pageNum: Int): MutableList<Worry>
    suspend fun getHistory(userId: String, pageNum: Int): MutableList<Worry>
    suspend fun getWorrys(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun keywordSearch(tagName: String, pageNum: Int): MutableList<Worry>
    suspend fun getWorryById(postId: Long): Worry
    suspend fun addOrUpdateWorry(contents: Contents)
    suspend fun postImage(file: MultipartBody.Part): String

    suspend fun deleteWorry(postId: Long)

    suspend fun getComments(postId: Long, pageNum: Int, userId: String): MutableList<Counsel>
    suspend fun addComment(counselRequestPojo: COUNSEL_REQUEST_POJO)
    suspend fun likeComment(commentId: Long, userId: String)
    suspend fun getStorys(): MutableList<Worry>
}