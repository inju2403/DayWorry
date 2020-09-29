package com.inju.dayworry.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.utils.Constants.TAG
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.util.helper.Utility.getPackageInfo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        kakao_login_button.setOnClickListener {
//            var keyHash = getHashKey(this)
//            Log.d(TAG, keyHash
            tryKaKaoLogin()
        }

        naver_login_button.setOnClickListener {
            //임시 라우팅
            startActivity(Intent(this, SetProfileActivity::class.java))
            finish()
        }

        skipText.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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

    private fun tryKaKaoLogin() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(this))
            .flatMap { available ->
                if (available) LoginClient.rx.loginWithKakaoTalk(this)
                else LoginClient.rx.loginWithKakaoAccount(this)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
//                var toast = Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.BOTTOM, 0,300)
//                toast.show()

                // 우리 서버로 카카오 토큰 보내고 jwt 받는 로직 필요

                //이미 프로필 설정 한번 했는지에 대한 처리 필요요
                startActivity(Intent(this, SetProfileActivity::class.java))
                finish()
            }, { error ->
                Log.e(TAG, "로그인 실패", error)
            })
            .addTo(disposables)
    }


    fun getHashKey(context: Context): String? {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNING_CERTIFICATES)
                val signatures = packageInfo.signingInfo.apkContentsSigners
                val md = MessageDigest.getInstance("SHA")
                for (signature in signatures) {
                    md.update(signature.toByteArray())
                    return String(Base64.encode(md.digest(), NO_WRAP))
                }
            } else {
                val packageInfo =
                    getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

                for (signature in packageInfo!!.signatures) {
                    try {
                        val md = MessageDigest.getInstance("SHA")
                        md.update(signature.toByteArray())
                        return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    } catch (e: NoSuchAlgorithmException) {
                        // ERROR LOG
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }
}