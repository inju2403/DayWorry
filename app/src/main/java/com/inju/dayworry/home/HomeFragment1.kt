package com.inju.dayworry.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import kotlinx.android.synthetic.main.fragment_home1.*

class HomeFragment1(worry: Worry) : Fragment() {

    private var handler: Handler? = null
    private var runnable: Runnable? =null
    private var worry = worry

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeLoadingUi.visibility = View.GONE

        setUi()

        lookIntoBtn.setOnClickListener {
            startActivity(Intent(activity!!, WorryDetailActivity::class.java))
        }
    }

    private fun setUi() {
//        worryImage = worry.
    }
}
