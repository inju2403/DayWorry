package com.inju.dayworry.login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.inju.dayworry.R
import android.util.Log
import com.inju.dayworry.utils.Constants.TAG
import kotlinx.android.synthetic.main.activity_add_worry.*
import kotlinx.android.synthetic.main.fragment_set_profile.*

class SetProfileFragment : Fragment() {

    var litePupleColor = "#9689FC"
    var superLiteGreyColor = "#cbcdd5"
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"

    var ageList = arrayOf("연령을 선택해주세요", "1~9", "10~19", "20~29", "30~39", "40~49", "50~59", "60~69", "70~")
    lateinit var userAge: String
    lateinit var userName: String

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
            (activity as SetProfileActivity).fragmentState = (activity as SetProfileActivity).FRAG_TAG
        }

        setTextChangeListener()
        setSpinner()

    }

    private fun moveSetTagFragment() {
        (activity as SetProfileActivity).switchSetTagFragment()
    }

    private fun setTextChangeListener() {

        editTextTextPersonName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() != "") {
                    userName = s.toString()
                    if(userAge != "") {
                        nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                        nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                    }
                    else {
                        nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                        nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                    }
                }
                else {
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                    userName = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setSpinner() {
        ageSpinner.adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, ageList)

        //아이템 선택 리스너
        ageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                userAge = if(position > 0) ageList[position] else ""
                if(userAge != "") {
                    if(editTextTextPersonName.text.toString() != "") {
                        nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                        nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                    }
                    else {
                        nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                        nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                    }
                }
                else {
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
        }
    }

}