package com.inju.dayworry

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import io.realm.Realm

class DayWorryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        // Kakao SDK 초기화
        KakaoSdk.init(this, "7092ff209929c21641987ead5b2bd0fc")
    }
}