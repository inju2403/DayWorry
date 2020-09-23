package com.inju.dayworry.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.counsel.CounselDetailActivity
import kotlinx.android.synthetic.main.fragment_home1.*

class HomeFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tmpImage.setOnClickListener {
            startActivity(Intent(activity!!, CounselDetailActivity::class.java))
        }
    }
}