package com.inju.dayworry.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import kotlinx.android.synthetic.main.fragment_set_profile.*
import kotlinx.android.synthetic.main.fragment_set_profile.nextBtn
import kotlinx.android.synthetic.main.fragment_set_tag.*

class SetTagFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_tag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preBtn.setOnClickListener {
            moveSetProfileFragment()
        }

        nextBtn.setOnClickListener {
            moveMainActivity()
        }
    }

    private fun moveSetProfileFragment() {
        (activity as SetProfileActivity).switchSetProfileFragment()
    }

    private fun moveMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        activity!!.finish()
    }
}