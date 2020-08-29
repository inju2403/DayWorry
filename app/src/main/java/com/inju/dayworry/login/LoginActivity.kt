package com.inju.dayworry.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworry.R
import com.inju.dayworry.utils.Constants.TAG
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val disposables = CompositeDisposable()

        login_button.setOnClickListener {

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(this))
                .flatMap { available ->
                    if (available) LoginClient.rx.loginWithKakaoTalk(this)
                    else LoginClient.rx.loginWithKakaoAccount(this)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ token ->
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                }, { error ->
                    Log.e(TAG, "로그인 실패", error)
                })
                .addTo(disposables)
        }

//        // 로그아웃
//        UserApiClient.rx.logout()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제 됨")
//            }, { error ->
//                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제 됨", error)
//            }).addTo(disposables)
//
//        // 연결 끊기
//        UserApiClient.rx.unlink()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//            }, { error ->
//                Log.e(TAG, "연결 끊기 실패", error)
//            }).addTo(disposables)
//
//        // 토큰 정보 보기
//        UserApiClient.rx.accessTokenInfo()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ tokenInfo ->
//                Log.i(TAG, "토큰 정보 보기 성공" +
//                        "\n회원번호: ${tokenInfo.id}" +
//                        "\n만료시간: ${tokenInfo.expiresIn} 초")
//            }, { error ->
//                Log.e(TAG, "토큰 정보 보기 실패", error)
//            })
//            .addTo(disposables)

    }
}