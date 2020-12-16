package com.inju.dayworryandroid.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inju.dayworryandroid.R

class SetProfileActivity : AppCompatActivity() {

    private val signUpFragment = SignUpFragment()
    private val setProfileFragment = SetProfileFragment()
    private val setTagFragment = SetTagFragment()
    private val fragmentManager = supportFragmentManager

    val getSignUpFragment get() = signUpFragment
    val getSetProfileFragment get() = setProfileFragment
    val getSetTagFragment get() = setTagFragment
    val getFragmentManager get() = fragmentManager

    val FRAG_SIGNUP = 0
    val FRAG_PROFILE = 1
    val FRAG_TAG = 2

    var fragmentState: Int = FRAG_PROFILE

    var userAge: String = ""
    var userName: String = ""
    var userId: String = ""
    var userPw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile)

        setFragment()
    }

    private fun setFragment() {
        fragmentManager.beginTransaction().replace(R.id.contentFrame, signUpFragment).commit()

        fragmentManager.beginTransaction().add(R.id.contentFrame, setProfileFragment).commit()
        fragmentManager.beginTransaction().hide(setProfileFragment).commit()

        fragmentManager.beginTransaction().add(R.id.contentFrame, setTagFragment).commit()
        fragmentManager.beginTransaction().hide(setTagFragment).commit()
    }

    fun switchSignUpFragment() {
        fragmentManager.beginTransaction().show(signUpFragment).commit()
        fragmentManager.beginTransaction().hide(setProfileFragment).commit()
        fragmentManager.beginTransaction().hide(setTagFragment).commit()
    }
    fun switchSetTagFragment() {
        fragmentManager.beginTransaction().hide(signUpFragment).commit()
        fragmentManager.beginTransaction().hide(setProfileFragment).commit()
        fragmentManager.beginTransaction().show(setTagFragment).commit()
    }
    fun switchSetProfileFragment() {
        fragmentManager.beginTransaction().hide(signUpFragment).commit()
        fragmentManager.beginTransaction().show(setProfileFragment).commit()
        fragmentManager.beginTransaction().hide(setTagFragment).commit()
    }

    override fun onBackPressed() {
        when (fragmentState) {
            FRAG_PROFILE -> {
                switchSignUpFragment()
                fragmentState = FRAG_SIGNUP
            }
            FRAG_TAG -> {
                switchSetProfileFragment()
                fragmentState = FRAG_PROFILE
            }
            else -> super.onBackPressed()
        }
    }
}