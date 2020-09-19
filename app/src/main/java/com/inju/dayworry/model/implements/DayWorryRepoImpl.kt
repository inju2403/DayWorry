package com.inju.dayworry.model.implements

import android.content.Context
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.model.repository.IDayWorryRepository
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

    var list = mutableListOf<Worry>()

    //local
    private val realm : Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun getWorrys(): MutableList<Worry> {
//        var worry = realm.where(Worry::class.java)
//            .sort("createdAt", Sort.DESCENDING)
//            .findFirst() as Worry
//        list.add(worry)
        return list
    }

    override fun getWorryByIdWorry(id : String) : Worry {
//        return realm.where(Worry::class.java)
//            .equalTo("id", id)
//            .findFirst() as Worry
        return Worry()
    }

    override fun addOrUpdateWorry(worry : Worry)  {
//        realm.executeTransaction {
//            it.copyToRealmOrUpdate(worry)
//        }
    }

    override fun deleteWorry(id : String) {
//        realm.executeTransaction {
//            it.where(Worry::class.java)
//                .equalTo("id", id)
//                .findFirst()?.let {
//                    it?.deleteFromRealm()
//                }
//        }
    }

}