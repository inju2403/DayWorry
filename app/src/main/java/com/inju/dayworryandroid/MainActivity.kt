package com.inju.dayworryandroid

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworryandroid.home.HomeListFragment
import com.inju.dayworryandroid.login.LoginActivity
import com.inju.dayworryandroid.mypage.MyPageFragment
import com.inju.dayworryandroid.notification.NotiFragment
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.utils.Constants.TAG
import com.inju.dayworryandroid.utils.Constants.PREFERENCE
import com.inju.dayworryandroid.worry.worryList.WorryListFragment
import com.inju.dayworryandroid.worry.worryDetail.AddWorryActivity
import com.inju.dayworryandroid.worry.worryList.WorryListViewModel
import com.inju.dayworryandroid.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.activity_main.*

@RequiresApi(26)
class MainActivity : AppCompatActivity() {

    private var worryListViewModel: WorryListViewModel ?= null
    val getWorryListViewModel get() = worryListViewModel

    private val counselFragment = HomeListFragment()
    private val worryListFragment = WorryListFragment()
    private val notiFragment = NotiFragment()
    private val myPageFragment = MyPageFragment()

    private val FRAG_COUNSEL = 0
    private val FRAG_WORRY = 1
    private val FRAG_NOTIFICATION = 2
    private val FRAG_MYPAGE = 3

    private val REQUEST_CODE = 101
    private val RETURN_OK = 101

    private var counselJudge = 1 //선택: 1, 미선택: 0
    private var worryJudge = 0
    private var notiJudge = 0
    private var myPageJudge = 0

    private var statusBarColor = "main"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val jwt = pref.getString("jwt", "").toString()

        setViewModel()
        setFragment()
        setBottomBar(jwt)
    }

    private fun setViewModel() {
        worryListViewModel = application!!.let {
            ViewModelProvider(viewModelStore, WorryListInjector(
                this.application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }


    private fun setFragment() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.contentFrame, counselFragment).commit()

        fragmentManager.beginTransaction().add(R.id.contentFrame, myPageFragment).commit()
        fragmentManager.beginTransaction().hide(myPageFragment).commit()

        fragmentManager.beginTransaction().add(R.id.contentFrame, notiFragment).commit()
        fragmentManager.beginTransaction().hide(notiFragment).commit()

        fragmentManager.beginTransaction().add(R.id.contentFrame, worryListFragment).commit()
        fragmentManager.beginTransaction().hide(worryListFragment).commit()
    }

    private fun setBottomBar(jwt: String) {

        counselTapView.setOnClickListener {
            if(counselJudge == 0) {
                counselJudge = 1
                worryJudge = 0
                notiJudge = 0
                myPageJudge = 0
                counselTapView.setImageResource(R.drawable.ic_home_checked)
                worryTapView.setImageResource(R.drawable.ic_worry_list_unchecked)
                notiTapView.setImageResource(R.drawable.ic_noti_unchecked)
                myPageTapView.setImageResource(R.drawable.ic_mypage_uncheced)

                setStatusBarColor("main")
                switchFragment(FRAG_COUNSEL)
            }
        }

        worryTapView.setOnClickListener {
            if(worryJudge == 0) {

                counselJudge = 0
                worryJudge = 1
                notiJudge = 0
                myPageJudge = 0
                counselTapView.setImageResource(R.drawable.ic_home_unchecked)
                worryTapView.setImageResource(R.drawable.ic_worry_list_checked)
                notiTapView.setImageResource(R.drawable.ic_noti_unchecked)
                myPageTapView.setImageResource(R.drawable.ic_mypage_uncheced)
                setStatusBarColor("dark")
                switchFragment(FRAG_WORRY)
            }
        }

        addTapView.setOnClickListener {
            if(jwt == "") {
                var toast = Toast.makeText(this@MainActivity, "로그인 후에 이용해주세요", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0,300)
                toast.show()

                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            else {
                setStatusBarColor("dark")
                startActivityForResult(Intent(this@MainActivity, AddWorryActivity::class.java), REQUEST_CODE)
            }
        }

        notiTapView.setOnClickListener {
            if(jwt == "") {
                var toast = Toast.makeText(this@MainActivity, "로그인 후에 이용해주세요", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0,300)
                toast.show()

                setStatusBarColor("main")
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            else {
                if (notiJudge == 0) {
                    counselJudge = 0
                    worryJudge = 0
                    notiJudge = 1
                    myPageJudge = 0
                    counselTapView.setImageResource(R.drawable.ic_home_unchecked)
                    worryTapView.setImageResource(R.drawable.ic_worry_list_unchecked)
                    notiTapView.setImageResource(R.drawable.ic_noti_checked)
                    myPageTapView.setImageResource(R.drawable.ic_mypage_uncheced)
                    setStatusBarColor("dark")
                    switchFragment(FRAG_NOTIFICATION)
                }
            }
        }

        myPageTapView.setOnClickListener {

            if(jwt == "") {
                var toast = Toast.makeText(this@MainActivity, "로그인 후에 이용해주세요", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0,300)
                toast.show()

                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                setStatusBarColor("main")
                finish()
            }
            else {
                if (myPageJudge == 0) {
                    counselJudge = 0
                    worryJudge = 0
                    notiJudge = 0
                    myPageJudge = 1
                    counselTapView.setImageResource(R.drawable.ic_home_unchecked)
                    worryTapView.setImageResource(R.drawable.ic_worry_list_unchecked)
                    notiTapView.setImageResource(R.drawable.ic_noti_unchecked)
                    myPageTapView.setImageResource(R.drawable.ic_mypage_checed)
                    setStatusBarColor("dark")
                    switchFragment(FRAG_MYPAGE)
                }
            }
        }
    }

    private fun switchFragment(next: Int) {
        val fragmentManager = supportFragmentManager
        when (next) {
            FRAG_COUNSEL -> {
                fragmentManager.beginTransaction().show(counselFragment).commit()
                fragmentManager.beginTransaction().hide(worryListFragment).commit()
                fragmentManager.beginTransaction().hide(notiFragment).commit()
                fragmentManager.beginTransaction().hide(myPageFragment).commit()
                statusBarColor = "main"
            }
            FRAG_WORRY -> {
                fragmentManager.beginTransaction().hide(counselFragment).commit()
                fragmentManager.beginTransaction().show(worryListFragment).commit()
                fragmentManager.beginTransaction().hide(notiFragment).commit()
                fragmentManager.beginTransaction().hide(myPageFragment).commit()
                statusBarColor = "dark"
            }
            FRAG_NOTIFICATION -> {
                fragmentManager.beginTransaction().hide(counselFragment).commit()
                fragmentManager.beginTransaction().hide(worryListFragment).commit()
                fragmentManager.beginTransaction().show(notiFragment).commit()
                fragmentManager.beginTransaction().hide(myPageFragment).commit()
                statusBarColor = "dark"
            }
            FRAG_MYPAGE -> {
                fragmentManager.beginTransaction().hide(counselFragment).commit()
                fragmentManager.beginTransaction().hide(worryListFragment).commit()
                fragmentManager.beginTransaction().hide(notiFragment).commit()
                fragmentManager.beginTransaction().show(myPageFragment).commit()
                statusBarColor = "dark"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE) {
            if(resultCode == RETURN_OK) {
                Log.d(TAG, "res ok")
                counselJudge = 0
                worryJudge = 1
                notiJudge = 0
                myPageJudge = 0
                counselTapView.setImageResource(R.drawable.ic_home_unchecked)
                worryTapView.setImageResource(R.drawable.ic_worry_list_checked)
                notiTapView.setImageResource(R.drawable.ic_noti_unchecked)
                myPageTapView.setImageResource(R.drawable.ic_mypage_uncheced)
                switchFragment(FRAG_WORRY)
            }
        }
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

    override fun onResume() {
        super.onResume()

        setStatusBarColor(statusBarColor)
    }
}
