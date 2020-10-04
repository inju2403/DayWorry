package com.inju.dayworry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.home.HomeListFragment
import com.inju.dayworry.mypage.MyPageFragment
import com.inju.dayworry.notification.NotiFragment
import com.inju.dayworry.utils.Constants.PREFERENCE
import com.inju.dayworry.worry.worryList.WorryListFragment
import com.inju.dayworry.worry.worryDetail.AddWorryActivity
import com.inju.dayworry.worry.worryList.WorryListViewModel
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.activity_main.*

@RequiresApi(21)
class MainActivity : AppCompatActivity() {

    private var mainWorryListViewModel: WorryListViewModel ?= null
    private var worryListViewModel: WorryListViewModel ?= null
    private var myWorryListViewModel: WorryListViewModel?= null

    private val counselFragment = HomeListFragment()
    private val worryListFragment = WorryListFragment()
    private val notiFragment = NotiFragment()
    private val myPageFragment = MyPageFragment()

    private val FRAG_COUNSEL = 0
    private val FRAG_WORRY = 1
    private val FRAG_NOTIFICATION = 2
    private val FRAG_MYPAGE = 3

    private val REQUEST_MAINACTIVITY_CODE = 100
    private val RETURN_OK = 101

    private var counselJudge = 1 //선택: 1, 미선택: 0
    private var worryJudge = 0
    private var notiJudge = 0
    private var myPageJudge = 0


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
        mainWorryListViewModel = application!!.let {
            ViewModelProvider(viewModelStore, WorryListInjector(
                this.application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }

        worryListViewModel = application!!.let {
            ViewModelProvider(viewModelStore, WorryListInjector(
                this.application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }

        myWorryListViewModel = application!!.let {
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
                switchFragment(FRAG_WORRY)
            }
        }

        addTapView.setOnClickListener {
            if(jwt == "") {
                var toast = Toast.makeText(this@MainActivity, "로그인 후에 이용해주세요", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0,300)
                toast.show()
            }
            else startActivityForResult(Intent(this@MainActivity, AddWorryActivity::class.java), REQUEST_MAINACTIVITY_CODE)
        }

        notiTapView.setOnClickListener {
            if(jwt == "") {
                var toast = Toast.makeText(this@MainActivity, "로그인 후에 이용해주세요", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0,300)
                toast.show()
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
                    switchFragment(FRAG_NOTIFICATION)
                }
            }
        }

        myPageTapView.setOnClickListener {

            if(jwt == "") {
                var toast = Toast.makeText(this@MainActivity, "로그인 후에 이용해주세요", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0,300)
                toast.show()
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
            }
            FRAG_WORRY -> {
                fragmentManager.beginTransaction().hide(counselFragment).commit()
                fragmentManager.beginTransaction().show(worryListFragment).commit()
                fragmentManager.beginTransaction().hide(notiFragment).commit()
                fragmentManager.beginTransaction().hide(myPageFragment).commit()
            }
            FRAG_NOTIFICATION -> {
                fragmentManager.beginTransaction().hide(counselFragment).commit()
                fragmentManager.beginTransaction().hide(worryListFragment).commit()
                fragmentManager.beginTransaction().show(notiFragment).commit()
                fragmentManager.beginTransaction().hide(myPageFragment).commit()
            }
            FRAG_MYPAGE -> {
                fragmentManager.beginTransaction().hide(counselFragment).commit()
                fragmentManager.beginTransaction().hide(worryListFragment).commit()
                fragmentManager.beginTransaction().hide(notiFragment).commit()
                fragmentManager.beginTransaction().show(myPageFragment).commit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_MAINACTIVITY_CODE) {
            if(resultCode == RETURN_OK) {
                counselJudge = 0
                worryJudge = 1
                notiJudge = 0
                myPageJudge = 0
                counselTapView.setImageResource(R.drawable.ic_home_unchecked)
                worryTapView.setImageResource(R.drawable.ic_worry_list_unchecked)
                notiTapView.setImageResource(R.drawable.ic_noti_unchecked)
                myPageTapView.setImageResource(R.drawable.ic_mypage_uncheced)
                switchFragment(FRAG_WORRY)
            }
        }
    }
}
