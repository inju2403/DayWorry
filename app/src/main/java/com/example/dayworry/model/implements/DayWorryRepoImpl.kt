package com.example.dayworry.model.implements

import android.content.Context
import com.example.dayworry.model.Worry
import com.example.dayworry.model.repository.IDayWorryRepository
import io.realm.Realm
import io.realm.Sort

class DayWorryRepoImpl(
//    val httpCall: ApiService?
//    = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(ApiService::class.java),
    val context: Context
): IDayWorryRepository
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