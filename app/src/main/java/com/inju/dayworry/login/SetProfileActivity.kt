package com.inju.dayworry.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inju.dayworry.R
import com.inju.dayworry.mypage.MyPageFragment
import com.inju.dayworry.worry.worryList.WorryListFragment

class SetProfileActivity : AppCompatActivity() {

    private val setProfileFragment = SetProfileFragment()
    private val setTagFragment = SetTagFragment()
    private val fragmentManager = supportFragmentManager

    val getSetProfileFragment get() = setProfileFragment
    val getSetTagFragment get() = setTagFragment
    val getFragmentManager get() = fragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile)

        setFragment()
    }

    private fun setFragment() {
        fragmentManager.beginTransaction().replace(R.id.contentFrame, setProfileFragment).commit()

        fragmentManager.beginTransaction().add(R.id.contentFrame, setTagFragment).commit()
        fragmentManager.beginTransaction().hide(setTagFragment).commit()
    }

    fun switchSetTagFragment() {
        fragmentManager.beginTransaction().hide(setProfileFragment).commit()
        fragmentManager.beginTransaction().show(setTagFragment).commit()
    }
    fun switchSetProfileFragment() {
        fragmentManager.beginTransaction().show(setProfileFragment).commit()
        fragmentManager.beginTransaction().hide(setTagFragment).commit()
    }
}