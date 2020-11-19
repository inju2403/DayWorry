package com.inju.dayworryandroid.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworryandroid.MainActivity
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.model.pojo.SOCIAL_LOGIN_RETURN_POJO
import com.inju.dayworryandroid.model.pojo.User_REQUEST_POJO
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.utils.Constants.API_BASE_URL
import com.inju.dayworryandroid.utils.Constants.PREFERENCE
import com.inju.dayworryandroid.utils.Constants.TAG
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.kakao.util.helper.Utility.getPackageInfo
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.coroutines.CoroutineContext


class LoginActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val disposables = CompositeDisposable()
    private val httpCall: ApiService? = RetrofitClient.getClient(API_BASE_URL)!!.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var keyHash = getHashKey(this)
        Log.d(TAG, keyHash)

        job = Job()

        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()

        kakao_login_button.setOnClickListener {
            editor.putString("social", "kakao")
            editor.commit()
            tryKaKaoLogin(pref)
        }

        naver_login_button.setOnClickListener {
            editor.putString("social", "naver")
            editor.commit()
            tryNaverLogin(pref)
        }

        skipText.setOnClickListener {
            editor.clear()
            editor.putBoolean("runFirst", false)
            editor.commit()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
//                verifyJWT(token.accessToken, pref)
                getUserTokenUsingKakaoToken(token.accessToken, pref)

            }, { error ->
                Log.e(TAG, "로그인 실패", error)
            })
            .addTo(disposables)
    }

    private fun getUserTokenUsingKakaoToken(token: String, pref: SharedPreferences) = launch {

        val editor = pref.edit()

        Log.d(TAG, "param token ${token}")
        httpCall?.kakaoLogin(token)?.enqueue(object : Callback<SOCIAL_LOGIN_RETURN_POJO> {
            override fun onFailure(call: Call<SOCIAL_LOGIN_RETURN_POJO>, t: Throwable) {
                Log.d(TAG, "kakaologin - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<SOCIAL_LOGIN_RETURN_POJO>, response: Response<SOCIAL_LOGIN_RETURN_POJO>) {
                when (response!!.code()) {
                    200 -> {
                        getUserIdByKakao(pref)
                        editor.putString("jwt", response.body()?.jwt.toString())
                        editor.putString("social", "kakao")
                        editor.commit()

                        verifyJWT(response.body()?.jwt.toString(), pref)

//                        Log.d(TAG,"jwt: ${response.body()?.jwt.toString()}")
//                        startActivity(Intent(this@LoginActivity, SetProfileActivity::class.java))
//                        finish()
                    }
                    400 -> {
                        Log.d(TAG, "kakaologin - 400 onFailed() called / t: ${response.body()}")
                        Toast.makeText(this@LoginActivity, "계정 정보를 확인해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }

        })

    }

    private fun getUserIdByKakao(pref: SharedPreferences) {
        val editor = pref.edit()
        // 토큰 정보 보기
        UserApiClient.rx.accessTokenInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tokenInfo ->
                Log.i(TAG, "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")
                editor.putLong("userId", tokenInfo.id)
                editor.commit()
            }, { error ->
                Log.e(TAG, "토큰 정보 보기 실패", error)
            })
            .addTo(disposables)
    }

    private fun verifyJWT(jwt: String, pref: SharedPreferences) = launch {
        val editor = pref.edit()
        httpCall?.verifyJWT(jwt)?.enqueue(object : Callback<User_REQUEST_POJO> {
            override fun onFailure(call: Call<User_REQUEST_POJO>, t: Throwable) {
                Log.d(TAG, "kakaologin - onFailed() called / t: ${t}")
                tryKaKaoLogin(pref)
            }

            override fun onResponse(call: Call<User_REQUEST_POJO>, response: Response<User_REQUEST_POJO>) {
                when (response.code()) {
                    200 -> {
                        Log.d("로그그","${response.body()!!.ageRange}")
                        if(response.body()!!.ageRange != 0) {
                            //이미 프로필 세팅한 적이 있으면
                            editor.putInt("userAge", response.body()!!.ageRange)
                            editor.putString("userName", response.body()!!.nickname)
                            editor.putString("profileImage", response.body()!!.profileImage)
                            editor.putLong("userId", response.body()!!.userId)

                            var hashTag = response.body()!!.userHashtags
                            var hashTagString = ""
                            for (next in hashTag) {
                                hashTagString += "$next,"
                            }
                            hashTagString = hashTagString.substring(0..hashTagString.length-2) // Split으로 parsing하여 사용
                            Log.d(TAG, hashTagString)
                            editor.putString("hashTags",hashTagString)
                            editor.commit()

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        else {
                            startActivity(Intent(this@LoginActivity, SetProfileActivity::class.java))
                            finish()
                        }
                    }
                    else -> {
                        startActivity(Intent(this@LoginActivity, SetProfileActivity::class.java))
                        finish()
                        Log.d(TAG,"토큰 유효하지 않음")
                    }
                }
            }

        })
    }

    private fun tryNaverLogin(pref: SharedPreferences) {
        /**
         * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
        객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
         */
        /**
         * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
         * 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
         */
        var mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
            this
            ,getString(R.string.naver_client_id)
            ,getString(R.string.naver_client_secret)
            ,getString(R.string.naver_client_name)
            //,OAUTH_CALLBACK_INTENT
            // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        )

        val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken: String = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                    val refreshToken: String = mOAuthLoginModule.getRefreshToken(this@LoginActivity)
                    val expiresAt: Long = mOAuthLoginModule.getExpiresAt(this@LoginActivity)
                    val tokenType: String = mOAuthLoginModule.getTokenType(this@LoginActivity)

//                    verifyJWT(accessToken, pref)
                    getUserTokenUsingNaverToken(accessToken, pref)

                    Log.d(TAG,"naver token: $accessToken")
                } else {
                    val errorCode: String =
                        mOAuthLoginModule.getLastErrorCode(this@LoginActivity).code
                    val errorDesc: String = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                    Toast.makeText(
                        this@LoginActivity, "errorCode:" + errorCode
                                + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
    }

    private fun getUserTokenUsingNaverToken(token: String, pref: SharedPreferences) = launch {
        val editor = pref.edit()
        var naverHttpCall = RetrofitClient.getNaverClient("https://openapi.naver.com/")!!.create(ApiService::class.java)
        var jsonObj = naverHttpCall.getNaverInfo("Bearer $token")

        var naverUserId = jsonObj.asJsonObject.getAsJsonObject("response").get("id").toString()
        naverUserId = naverUserId.substring(1..naverUserId.length-2)
        editor.putLong("userId", naverUserId.toLong())
        editor.commit()
        Log.d(TAG, "naver user id: $naverUserId")

        Log.d(TAG, "param token ${token}")
        httpCall?.naverLogin(token)?.enqueue(object : Callback<SOCIAL_LOGIN_RETURN_POJO> {
            override fun onFailure(call: Call<SOCIAL_LOGIN_RETURN_POJO>, t: Throwable) {
                Log.d(TAG, "naverlogin - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<SOCIAL_LOGIN_RETURN_POJO>, response: Response<SOCIAL_LOGIN_RETURN_POJO>) {
                when (response.code()) {
                    200 -> {
                        editor.putString("jwt", response.body()?.jwt.toString())
                        editor.putString("social", "naver")
                        editor.commit()

                        verifyJWT(response.body()?.jwt.toString() ,pref)


//                        Log.d(TAG,"jwt: ${response.body()?.jwt.toString()}")
//                        if(response.body()?.jwt.toString() != "null") {
//                            startActivity(
//                                Intent(
//                                    this@LoginActivity,
//                                    SetProfileActivity::class.java
//                                )
//                            )
//                            finish()
//                        }
//                        else Toast.makeText(this@LoginActivity, "계정 정보를 확인해주세요", Toast.LENGTH_LONG).show()
                    }
                    400 -> {
                        Log.d(TAG, "naverlogin - 400 onFailed() called / t: ${response.body()}")
                        Toast.makeText(this@LoginActivity, "계정 정보를 확인해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }

        })

    }


    fun getHashKey(context: Context): String? {
        //사용 예
        //    var keyHash = getHashKey(this)
        //    Log.d(TAG, keyHash)

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

    private fun setStatusBarColor() {
        var view = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(view != null) {
                window.statusBarColor = Color.parseColor(Constants.mainNaviColor)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setStatusBarColor()
    }


    //카카오
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