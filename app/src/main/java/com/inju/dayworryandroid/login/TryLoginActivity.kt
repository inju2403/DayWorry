package com.inju.dayworryandroid.login

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.utils.Constants

class TryLoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_login)

        setStatusBarColor("dark")
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
}