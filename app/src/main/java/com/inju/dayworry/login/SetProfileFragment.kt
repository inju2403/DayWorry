package com.inju.dayworry.login

import android.content.SharedPreferences
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
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.Constants.TAG
import com.inju.dayworry.utils.EditBottomSheetFragment
import com.inju.dayworry.utils.SelectProfilePhotoBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_set_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class SetProfileFragment: Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var litePupleColor = "#9689FC"
    var superLiteGreyColor = "#cbcdd5"
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"

    private val httpCall: ApiService? = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
        ApiService::class.java)

    private var judge = true

    private val nicknameEmptyMessage = "닉네임을 입력해주세요"
    private val nicknameRedundancyMessage = "이미 존재하는 닉네임입니다"
    private val ageMessage = "연령을 선택해주세요"
    private val setImageMessage = "프로필 이미지를 선택해주세요"

    var ageList = arrayOf("연령을 선택해주세요", "1~9", "10~19", "20~29", "30~39", "40~49", "50~59", "60~69", "70~")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        job = Job()

        // Glide로 이미지 표시하기
//        var imageUrl = "https://d3scsscaxt5rdy.cloudfront.net/hago.png-20205304125305"
//        Glide.with(this).load(imageUrl).into(profile_photo)

        val pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)

        setUpClickListener(pref)

        setTextChangeListener()
        setSpinner()

    }

    private fun setUpClickListener(pref: SharedPreferences) {

        profile_photo.setOnClickListener {
            val selectProfilePhotoBottomSheetFragment = SelectProfilePhotoBottomSheetFragment(profile_photo)

            selectProfilePhotoBottomSheetFragment.show(activity!!.supportFragmentManager, "selectProfilePhotoBottomSheetFragment")
        }

        profile_photo_change_image.setOnClickListener {
            val selectProfilePhotoBottomSheetFragment = SelectProfilePhotoBottomSheetFragment(profile_photo)

            selectProfilePhotoBottomSheetFragment.show(activity!!.supportFragmentManager, "selectProfilePhotoBottomSheetFragment")
        }

        nextBtn.setOnClickListener {
            var profileImageSet = pref.getString("profileImageSet", "no")

            when {
                (activity as SetProfileActivity).userName == "" -> {
                    showToast(nicknameEmptyMessage)
                }
                (activity as SetProfileActivity).userAge == "" -> {
                    showToast(ageMessage)
                }
                profileImageSet == "no" -> {
                    showToast(setImageMessage)
                }
                else -> {
                    judgeUserName((activity as SetProfileActivity).userName)
                }
            }
        }
    }

    private fun judgeUserName(userName: String) = launch {
        judge = httpCall?.nicknameRedundancyCheck(userName)?.flag!!
        Log.d(TAG,"judge: $judge")
        if(judge) {
            moveSetTagFragment()
            (activity as SetProfileActivity).fragmentState =
                (activity as SetProfileActivity).FRAG_TAG
        }
        else showToast(nicknameRedundancyMessage)
    }

    private fun moveSetTagFragment() {
        (activity as SetProfileActivity).switchSetTagFragment()
    }

    private fun setTextChangeListener() {

        editTextTextPersonName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() != "") {
                    (activity as SetProfileActivity).userName = s.toString()
                    if((activity as SetProfileActivity).userAge != "") {
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
                    (activity as SetProfileActivity).userName = ""
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
                (activity as SetProfileActivity).userAge = if(position > 0) ageList[position] else ""
                if((activity as SetProfileActivity).userAge != "") {
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

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity!!, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }

}