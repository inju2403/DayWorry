package com.inju.dayworry.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.inju.dayworry.model.pojo.KAKAO_RETURN_POJO
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants.API_BASE_URL
import com.inju.dayworry.utils.Constants.PREFERENCE
import com.inju.dayworry.utils.Constants.TAG
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.util.helper.Utility.getPackageInfo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private val httpCall: ApiService? = RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)

        kakao_login_button.setOnClickListener {

//            var keyHash = getHashKey(this)
//            Log.d(TAG, keyHash)
            val jwt = pref.getString("jwt", "").toString()
            Log.d(TAG,"jwt: $jwt")
            if(jwt != "") {
                //jwt가 아직 유효한지 검사 (다른기기에서 로그인했을시에 만료되기 때문)
                verifyJWT(jwt, pref)
            }
            else tryKaKaoLogin(pref)
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

    private fun tryKaKaoLogin(pref: SharedPreferences) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(this))
            .flatMap { available ->
                if (available) LoginClient.rx.loginWithKakaoTalk(this)
                else LoginClient.rx.loginWithKakaoAccount(this)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                getUserTokenUsingKakaoToken(token.accessToken, pref)

            }, { error ->
                Log.e(TAG, "로그인 실패", error)
            })
            .addTo(disposables)
    }

    fun getUserTokenUsingKakaoToken(token: String, pref: SharedPreferences) {
        val editor = pref.edit()

        Log.d(TAG, "param token ${token}")
        httpCall?.kakaoLogin(token)?.enqueue(object : Callback<KAKAO_RETURN_POJO> {
            override fun onFailure(call: Call<KAKAO_RETURN_POJO>, t: Throwable) {
                Log.d(TAG, "kakaologin - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<KAKAO_RETURN_POJO>, response: Response<KAKAO_RETURN_POJO>) {
                when (response!!.code()) {
                    200 -> {
                        editor.putString("jwt", response.body()?.jwt.toString())
                        editor.commit()
                        var toast = Toast.makeText(this@LoginActivity, "로그인 jwt: ${response.body()?.jwt}", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.BOTTOM, 0,300)
                        toast.show()


                        Log.d(TAG,response.body().toString())
                        startActivity(Intent(this@LoginActivity, SetProfileActivity::class.java))
                        finish()
                    }
                    401 -> {
                        Log.d(TAG, "kakaologin - 401 onFailed() called / t: ${response.body()}")
                        Toast.makeText(this@LoginActivity, "계정 정보를 확인해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }

        })

    }

    fun verifyJWT(jwt: String, pref: SharedPreferences) {
        httpCall?.verifyJWT(jwt)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "kakaologin - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when (response!!.code()) {
                    200 -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    401 -> {
                        tryKaKaoLogin(pref)
                    }
                }
            }

        })
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