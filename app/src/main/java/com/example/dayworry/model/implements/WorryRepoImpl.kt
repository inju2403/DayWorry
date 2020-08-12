package com.example.dayworry.model.implements

import android.content.Context
import android.util.Log
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IWorryRepository
import com.example.dayworry.utils.Constants
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class WorryRepoImpl(
//    val httpCall: ApiService?
//    = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(ApiService::class.java),
    val context: Context
): IWorryRepository
{
//    var tmpWorrys = mutableListOf<Worry>()
//
//    override fun getWorrys(): MutableList<Worry> {
//        return tmpWorrys
//    }

    //local
    private val realm : Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun getWorrys(): MutableList<Worry> {
        return realm.where(Worry::class.java)
            .sort("createdAt", Sort.DESCENDING)
            .findAll()
    }

    override fun getWorryByIdWorry(id : String) : Worry {
        return realm.where(Worry::class.java)
            .equalTo("id", id)
            .findFirst() as Worry
    }

    override fun addOrUpdateWorry(worry : Worry)  {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(worry)
        }
    }

    override fun deleteWorry(id : String) {
        realm.executeTransaction {
            it.where(Worry::class.java)
                .equalTo("id", id)
                .findFirst()?.let {
                    it?.deleteFromRealm()
                }
        }
    }

}