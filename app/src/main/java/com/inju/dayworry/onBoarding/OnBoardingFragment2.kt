package com.inju.dayworry.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.inju.dayworry.R
import kotlinx.android.synthetic.main.fragment_on_boarding2.*

@RequiresApi(21)
class OnBoardingFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLottieView()

    }

    private fun showLottieView() {
        lottieView2.playAnimation()
        lottieView2.loop(true)
    }

}