package com.example.dayworry.model.repository

import android.content.Context
import com.example.dayworry.model.Worry
import io.realm.RealmResults

interface IWorryRepository {
    fun getWorrys(): MutableList<Worry>
    fun getWorryByIdWorry(id : String): Worry
    fun addOrUpdateWorry(worry : Worry)
    fun deleteWorry(id: String)
}