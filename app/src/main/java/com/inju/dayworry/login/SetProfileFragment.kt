package com.inju.dayworry.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inju.dayworry.R
import kotlinx.android.synthetic.main.fragment_set_profile.*

class SetProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nextBtn.setOnClickListener {
            moveSetTagFragment()
        }
    }

    private fun moveSetTagFragment() {
        (activity as SetProfileActivity).switchSetTagFragment()
    }
}