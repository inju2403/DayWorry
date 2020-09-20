package com.inju.dayworry.model.repository

import com.inju.dayworry.model.pojo.ContentsPOJO
import com.inju.dayworry.model.pojo.Worry

interface IDayWorryRepository {
    fun getWorrys(): MutableList<Worry>
    fun getWorryById(id : Long): Worry
    fun addOrUpdateWorry(contentsPOJO: ContentsPOJO, id: Long)
    fun deleteWorry(id: Long)
}