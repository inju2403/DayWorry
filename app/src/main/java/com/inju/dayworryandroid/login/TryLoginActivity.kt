package com.inju.dayworryandroid.login

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.google.gson.JsonElement
import com.inju.dayworryandroid.MainActivity
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.model.pojo.JWTPOJO
import com.inju.dayworryandroid.model.pojo.User_REQUEST_POJO
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants
import kotlinx.android.synthetic.main.activity_try_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TryLoginActivity : AppCompatActivity() {

    private var userId = ""
    private var userPw = ""

    var litePupleColor = "#9689FC"
    var superLiteGreyColor = "#cbcdd5"
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"

    private val httpCall: ApiService? = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
        ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_login)

        val pref = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE)

        setStatusBarColor("dark")
        loginLoadingUi.visibility = View.GONE

        setUpClickListener(pref)
        setTextChangeListener()
    }

    private fun setUpClickListener(pref: SharedPreferences) {
        val editor = pref.edit()
        loginBtn.setOnClickListener {

            when {
                userId.isEmpty() -> {
                    showToast("아이디를 입력해주세요")
                }
                userPw.isEmpty() -> {
                    showToast("비밀번호를 입력해주세요")
                }
                else -> {
                    loginLoadingUi.visibility = View.VISIBLE

                    httpCall?.login(userId, userPw)?.enqueue(object : Callback<JWTPOJO> {

                        override fun onFailure(call: Call<JWTPOJO>, t: Throwable) {
                            Log.d(Constants.TAG, "login - onFailed() called / t: ${t}")
                            showToast("잠시 후에 다시 시도해주세요")
                            loginLoadingUi.visibility = View.GONE
                        }

                        override fun onResponse(call: Call<JWTPOJO>, response: Response<JWTPOJO>) {
                            editor.putString("jwt", response.body()!!.jwt)
                            when(response.code()) {
                                200 -> {
                                    Log.d("로로", response.body()!!.jwt)
                                    httpCall.verifyUserToken(response.body()!!.jwt).enqueue(object : Callback<User_REQUEST_POJO> {

                                        override fun onFailure(call: Call<User_REQUEST_POJO>, t: Throwable) {
                                            Log.d(Constants.TAG, "verifyUserToken - onFailed() called / t: ${t}")
                                            showToast("잠시 후에 다시 시도해주세요")
                                            loginLoadingUi.visibility = View.GONE
                                        }

                                        override fun onResponse(call: Call<User_REQUEST_POJO>, response: Response<User_REQUEST_POJO>) {

                                            when(response.code()) {
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
                                                        hashTagString = hashTagString.substring(0..hashTagString.length-2) // Split으로 parsing하여 사용
                                                        Log.d(Constants.TAG, hashTagString)
                                                        editor.putString("hashTags",hashTagString)
                                                        editor.commit()

                                                        loginLoadingUi.visibility = View.GONE
                                                        startActivity(Intent(this@TryLoginActivity, MainActivity::class.java))
                                                        finish()
                                                    }
                                                }
                                                else -> {
                                                    loginLoadingUi.visibility = View.GONE
                                                    showToast("잠시 후에 다시 시도해주세요.")
                                                }
                                            }
                                        }

                                    })

                                }
                                else -> {
                                    showToast("잠시 후에 다시 시도해주세요.")
                                }
                            }
                        }

                    })
                }
            }
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this@TryLoginActivity, SetProfileActivity::class.java))
            finish()
        }
    }

    private fun setTextChangeListener() {
        idEditText.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                userId = s.toString().trim()

                if (userId.isNotEmpty() && userPw.isNotEmpty()) {
                    loginBtn.setBackgroundResource(R.drawable.next_btn_background)
                    loginBtn.setTextColor(Color.parseColor(darkNavyColor))
                } else {
                    loginBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    loginBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        pwEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                userPw = s.toString().trim()

                if (userId.isNotEmpty() && userPw.isNotEmpty()) {
                    loginBtn.setBackgroundResource(R.drawable.next_btn_background)
                    loginBtn.setTextColor(Color.parseColor(darkNavyColor))
                } else {
                    loginBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    loginBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setStatusBarColor(str: String) {
        var view = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(view != null) {
                when(str) {
                    "main" -> window.statusBarColor = Color.parseColor(Constants.mainNaviColor)
                    "dark" -> window.statusBarColor = Color.parseColor(Constants.darkNaviColor)
                }
            }
        }
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(this, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}