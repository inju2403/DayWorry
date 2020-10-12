package com.inju.dayworry

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.inju.dayworry.login.LoginActivity
import com.inju.dayworry.onBoarding.OnBoardingActivity
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.Constants.PREFERENCE
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
                    verifyJWT(jwt, pref)
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

    private fun verifyJWT(jwt: String, pref: SharedPreferences) {
        httpCall?.verifyJWT(jwt)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(Constants.TAG, "verifyJWT - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when (response.code()) {
                    200 -> {
                        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                        finish()
                    }
                    else -> {
                        Log.d(Constants.TAG,"토큰 유효하지 않음")
                        val editor = pref.edit()
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
}