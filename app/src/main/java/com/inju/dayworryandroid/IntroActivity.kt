package com.inju.dayworryandroid

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.inju.dayworryandroid.login.LoginActivity
import com.inju.dayworryandroid.model.pojo.User_REQUEST_POJO
import com.inju.dayworryandroid.onBoarding.OnBoardingActivity
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.utils.Constants.PREFERENCE
import kotlinx.android.synthetic.main.activity_intro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntroActivity : AppCompatActivity() {

    private var handler: Handler? = null
    private var runnable: Runnable? =null

    private val httpCall: ApiService? = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
        ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        showLottieView()
    }

    override fun onResume() {
        super.onResume()

        runnable = Runnable {
            val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
            val editor = pref.edit()
            var runFirst = pref.getBoolean("runFirst", true)

            if(runFirst) { // 최초 실행
                editor.putBoolean("runFirst", false)
                editor.commit()

                startActivity(Intent(this@IntroActivity, OnBoardingActivity::class.java))
                finish()
            }

            else {
                //최초 실행이 아니면
                val jwt = pref.getString("jwt", "").toString()
                if(jwt != "") {
                    //jwt가 아직 유효한지 검사
                    //유효하면 자동로그인
                    verifyUserToken(jwt, pref)
                }
                else {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        handler = Handler()
        handler?.run {
            postDelayed(runnable, 2000)
        }
    }

    private fun verifyUserToken(jwt: String, pref: SharedPreferences) {
        val editor = pref.edit()

        httpCall?.verifyUserToken(jwt)?.enqueue(object : Callback<User_REQUEST_POJO> {
            override fun onFailure(call: Call<User_REQUEST_POJO>, t: Throwable) {
                Log.d(Constants.TAG, "verifyJWT - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<User_REQUEST_POJO>, response: Response<User_REQUEST_POJO>) {
                when (response.code()) {
                    200 -> {
                        if(response.body()!!.ageRange != 0) {
                            editor.putInt("userAge", response.body()!!.ageRange)
                            editor.putString("userName", response.body()!!.nickname)
                            editor.putString("profileImage", response.body()!!.profileImage)
                            editor.putString("userId", response.body()!!.userId)

                            var hashTag = response.body()!!.userHashtags
                            var hashTagString = ""
                            for (next in hashTag) {
                                hashTagString += "$next,"
                            }
                            hashTagString =
                                hashTagString.substring(0..hashTagString.length - 2) // Split으로 parsing하여 사용
                            Log.d(Constants.TAG, hashTagString)
                            editor.putString("hashTags", hashTagString)
                            editor.commit()
                            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                    else -> {
                        Log.d(Constants.TAG,"토큰 유효하지 않음")
                        editor.clear()
                        editor.commit()

                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()

        handler?.removeCallbacks(runnable)
    }

    private fun showLottieView() {
        splashLottieView.playAnimation()
        splashLottieView.loop(true)
    }
}