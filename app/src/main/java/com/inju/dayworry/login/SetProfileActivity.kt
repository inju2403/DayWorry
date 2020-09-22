package com.inju.dayworry.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworry.R

class SetProfileActivity : AppCompatActivity() {

    private val setProfileFragment = SetProfileFragment()
    private val setTagFragment = SetTagFragment()
    private val fragmentManager = supportFragmentManager

    val getSetProfileFragment get() = setProfileFragment
    val getSetTagFragment get() = setTagFragment
    val getFragmentManager get() = fragmentManager

    val FRAG_PROFILE = 0
    val FRAG_TAG = 1

    var fragmentState: Int = FRAG_PROFILE

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

    override fun onBackPressed() {
        if(fragmentState == FRAG_TAG) {
            switchSetProfileFragment()
            fragmentState = FRAG_PROFILE
        }
        else super.onBackPressed()
    }
}