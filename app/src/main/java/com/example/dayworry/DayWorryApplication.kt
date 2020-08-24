package com.example.dayworry

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import io.realm.Realm

class DayWorryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        // Kakao SDK 초기화
        KakaoSdk.init(this, "{NATIVE_APP_KEY}")
    }
}