package com.inju.dayworryandroid.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.home.viewPager.DepthPageTransformer
import com.inju.dayworryandroid.login.LoginActivity
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.android.synthetic.main.fragment_home_list.dots_indicator

@RequiresApi(21)
class OnBoardingActivity : AppCompatActivity() {

    private val pageSize = 3
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        setViewPager()
        setUpClickListener()
        setMoveFragment()
    }

    private fun setViewPager()  {
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.onBoardingListViewPager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        viewPager.setPageTransformer(DepthPageTransformer())

        onBoardingListViewPager.apply {
            adapter = viewPager.adapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            dots_indicator.setViewPager2(viewPager)
        }

        onBoardingListViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position) {
                    0 -> {
                        nextBtn.text = "다음"
                        prevText.visibility = View.GONE
                        skipText.visibility = View.VISIBLE
                    }
                    pageSize - 1 -> {
                        nextBtn.text = "시작하기"
                        prevText.visibility = View.VISIBLE
                        skipText.visibility = View.GONE
                    }
                    else -> {
                        nextBtn.text = "다음"
                        skipText.visibility = View.VISIBLE
                        prevText.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            lateinit var frag: Fragment
            when(position) {
                0 -> frag = OnBoardingFragment1()
                1 -> frag = OnBoardingFragment2()
                2 -> frag = OnBoardingFragment3()
            }
            return frag
        }
    }

    private fun setUpClickListener() {
        prevText.setOnClickListener {
            onBoardingListViewPager.currentItem = onBoardingListViewPager.currentItem - 1
        }

        skipText.setOnClickListener {
            startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
            finish()
        }

        nextBtn.setOnClickListener {
            if(onBoardingListViewPager.currentItem == pageSize - 1) {
                startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                finish()
            }
            else onBoardingListViewPager.currentItem = onBoardingListViewPager.currentItem + 1
        }
    }

    private fun setMoveFragment() {

    }
}