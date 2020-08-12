package com.example.dayworry

import android.app.Application
import io.realm.Realm

class DayWorryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}