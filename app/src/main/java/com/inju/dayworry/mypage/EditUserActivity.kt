package com.inju.dayworry.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.User_REQUEST_POJO
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.SelectProfilePhotoBottomSheetFragment
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_edit_user.ageSpinner
import kotlinx.android.synthetic.main.activity_edit_user.courseBtn
import kotlinx.android.synthetic.main.activity_edit_user.dailyLiftBtn
import kotlinx.android.synthetic.main.activity_edit_user.dateBtn
import kotlinx.android.synthetic.main.activity_edit_user.editTextTextPersonName
import kotlinx.android.synthetic.main.activity_edit_user.employmentBtn
import kotlinx.android.synthetic.main.activity_edit_user.familyBtn
import kotlinx.android.synthetic.main.activity_edit_user.friendBtn
import kotlinx.android.synthetic.main.activity_edit_user.healthBtn
import kotlinx.android.synthetic.main.activity_edit_user.infantBtn
import kotlinx.android.synthetic.main.activity_edit_user.jobBtn
import kotlinx.android.synthetic.main.activity_edit_user.marriedBtn
import kotlinx.android.synthetic.main.activity_edit_user.moneyBtn
import kotlinx.android.synthetic.main.activity_edit_user.profile_photo
import kotlinx.android.synthetic.main.activity_edit_user.profile_photo_change_image
import kotlinx.android.synthetic.main.activity_edit_user.schoolBtn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class EditUserActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var judge = true

    private val httpCall: ApiService? =
        RetrofitClient.getClient(com.inju.dayworry.utils.Constants.API_BASE_URL)!!.create(
            ApiService::class.java
        )
    var litePupleColor = "#9689FC"
    var superLiteGreyColor = "#cbcdd5"
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"

    private val selectTagMinMessage = "고민을 한 가지 이상 선택해주세요"
    private val selectTagMaxMessage = "고민은 3개까지 선택할 수 있어요"

    private val nicknameEmptyMessage = "닉네임을 입력해주세요"
    private val nicknameRedundancyMessage = "이미 존재하는 닉네임입니다"

    var ageList = arrayOf("1~9", "10~19", "20~29", "30~39", "40~49", "50~59", "60~69", "70~")

    private var hashTag: MutableList<String> = mutableListOf()
    private var userAgeValue: Int = 0
    private var userAge: String = ""
    private var hashTagString: String = ""
    private var profileImage: String = ""
    private var userName: String = ""
    private var originUserName: String = ""

    private var userId: Long = 0

    private var totalCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        editUserLoadingUi.visibility = View.GONE
        job = Job()

        val pref = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE)
        val editor = pref.edit()
        userAgeValue = pref.getInt("userAge", 0)
        profileImage = pref.getString("profileImage", "empty").toString()
        userId = pref.getLong("userId", (0).toLong())
        userName = pref.getString("userName", "").toString()
        originUserName = pref.getString("userName", "").toString()
        hashTagString = pref.getString("hashTags", "").toString()

        var hashTagList = hashTagString.split(",")
        for (next in hashTagList) hashTag.add(next)


        setUpClickListener(editor)
        setSpinner()
        setUserInfo()
        setTextChangeListener()
        setTagBtn()
    }

    private fun setUpClickListener(editor: SharedPreferences.Editor) = launch {

        profile_photo.setOnClickListener {
            val selectProfilePhotoBottomSheetFragment = SelectProfilePhotoBottomSheetFragment(profile_photo)

            selectProfilePhotoBottomSheetFragment.show(supportFragmentManager, "selectProfilePhotoBottomSheetFragment")
        }

        profile_photo_change_image.setOnClickListener {
            val selectProfilePhotoBottomSheetFragment = SelectProfilePhotoBottomSheetFragment(profile_photo)

            selectProfilePhotoBottomSheetFragment.show(supportFragmentManager, "selectProfilePhotoBottomSheetFragment")
        }

        finishBtn.setOnClickListener {
            when {
                userName == "" -> showToast(nicknameEmptyMessage)
                totalCnt == 0 -> showToast(selectTagMinMessage)
                originUserName == userName -> {
                    editor.putString("userName", userName)
                    when(userAge) {
                        "1~9" -> userAgeValue = 0
                        "10~19" -> userAgeValue = 10
                        "20~29" -> userAgeValue = 20
                        "30~39" -> userAgeValue = 30
                        "40~49" -> userAgeValue = 40
                        "50~59" -> userAgeValue = 50
                        "60~69" -> userAgeValue = 60
                        "70~" -> userAgeValue = 70
                    }
                    editor.putInt("userAge", userAgeValue)
                    for (next in hashTag) {
                        hashTagString += "$next,"
                    }
                    hashTagString = hashTagString.substring(0..hashTagString.length-2) // Split으로 parsing하여 사용
                    editor.putString("hashTags", hashTagString)
                    editor.commit()

                    requsetProfileUpdate()
                }
                else -> judgeUserName(userName, editor)
            }
        }
    }

    private fun judgeUserName(userName: String, editor: SharedPreferences.Editor) = launch {
        judge = httpCall?.nicknameRedundancyCheck(userName)?.flag!!
        Log.d(Constants.TAG,"judge: $judge")

        if(judge) {
            editor.putString("userName", userName)
            when(userAge) {
                "1~9" -> userAgeValue = 0
                "10~19" -> userAgeValue = 10
                "20~29" -> userAgeValue = 20
                "30~39" -> userAgeValue = 30
                "40~49" -> userAgeValue = 40
                "50~59" -> userAgeValue = 50
                "60~69" -> userAgeValue = 60
                "70~" -> userAgeValue = 70
            }
            editor.putInt("userAge", userAgeValue)
            for (next in hashTag) {
                hashTagString += "$next,"
            }
            hashTagString = hashTagString.substring(0..hashTagString.length-2) // Split으로 parsing하여 사용
            editor.putString("hashTags", hashTagString)
            editor.commit()

            requsetProfileUpdate()
        }
        else showToast(nicknameRedundancyMessage)
    }

    private fun requsetProfileUpdate() {
        editUserLoadingUi.visibility = View.VISIBLE
        val userRequestPojo = User_REQUEST_POJO(
            userAgeValue,
            userName,
            profileImage,
            hashTag,
            userId)

        httpCall?.updateProfile(userRequestPojo)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(Constants.TAG, "updateProfile - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when (response.code()) {
                    200 -> {
                        showToast("프로필 변경이 완료되었습니다")
                        startActivity(Intent(this@EditUserActivity, MainActivity::class.java))
                        finish()
                    }
                    400 -> {
                        Log.d(Constants.TAG, "kakaologin - 400 onFailed() called / t: ${response.body()}")
                    }
                }
            }

        })
    }

    private fun setSpinner() {
        ageSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ageList)

        var userAgeIdx = 0
        if(userAgeValue > 0) userAgeIdx = userAgeValue / 10
        //원래 연령을 디폴트 값으로 지정
        ageSpinner.setSelection(userAgeIdx)

        //아이템 선택 리스너
        ageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                userAge = if(position > 0) ageList[position] else ""
                if(userAge != "") {
                    if(editTextTextPersonName.text.toString() != "") {
                        finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                        finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                    }
                    else {
                        finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                        finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                    }
                }
                else {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
        }
    }

    private fun setUserInfo() {
        editTextTextPersonName.setText(userName)

        Glide.with(this).load(profileImage).into(profile_photo)

        for(next in hashTag) {
            when(next) {
                "일상" -> {
                    dailyLiftBtn.isSelected = true
                    dailyLiftBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "가족" -> {
                    familyBtn.isSelected = true
                    familyBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    familyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "친구사이" -> {
                    friendBtn.isSelected = true
                    friendBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    friendBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "연애" -> {
                    dateBtn.isSelected = true
                    dateBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    dateBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "학교생활" -> {
                    schoolBtn.isSelected = true
                    schoolBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    schoolBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "직장생활" -> {
                    jobBtn.isSelected = true
                    jobBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    jobBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "취업" -> {
                    employmentBtn.isSelected = true
                    employmentBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    employmentBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "진로" -> {
                    courseBtn.isSelected = true
                    courseBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    courseBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "돈" -> {
                    moneyBtn.isSelected = true
                    moneyBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    moneyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "건강" -> {
                    healthBtn.isSelected = true
                    healthBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    healthBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "기혼자만 아는" -> {
                    marriedBtn.isSelected = true
                    marriedBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    marriedBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                "육아" -> {
                    infantBtn.isSelected = true
                    infantBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    infantBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }
    }

    private fun setTextChangeListener() {
        editTextTextPersonName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                userName = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setTagBtn() {
        dailyLiftBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dailyLiftBtn.isSelected = false
                dailyLiftBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("일상")
                    dailyLiftBtn.isSelected = true
                    dailyLiftBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        familyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                familyBtn.isSelected = false
                familyBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("가족")
                    familyBtn.isSelected = true
                    familyBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    familyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        friendBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                friendBtn.isSelected = false
                friendBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("친구사이")
                    friendBtn.isSelected = true
                    friendBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    friendBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        dateBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dateBtn.isSelected = false
                dateBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("연애")
                    dateBtn.isSelected = true
                    dateBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    dateBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        schoolBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                schoolBtn.isSelected = false
                schoolBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("학교생활")
                    schoolBtn.isSelected = true
                    schoolBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    schoolBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        jobBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                jobBtn.isSelected = false
                jobBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("직장생활")
                    jobBtn.isSelected = true
                    jobBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    jobBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        employmentBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                employmentBtn.isSelected = false
                employmentBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("취업")
                    employmentBtn.isSelected = true
                    employmentBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    employmentBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        courseBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                courseBtn.isSelected = false
                courseBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("진로")
                    courseBtn.isSelected = true
                    courseBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    courseBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        moneyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                moneyBtn.isSelected = false
                moneyBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("돈")
                    moneyBtn.isSelected = true
                    moneyBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    moneyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        healthBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                healthBtn.isSelected = false
                healthBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("건강")
                    healthBtn.isSelected = true
                    healthBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    healthBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        marriedBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                marriedBtn.isSelected = false
                marriedBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("기혼자만 아는")
                    marriedBtn.isSelected = true
                    marriedBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    marriedBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        infantBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                infantBtn.isSelected = false
                infantBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_unselect_style)
                infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    finishBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    finishBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("육아")
                    infantBtn.isSelected = true
                    infantBtn.background = resources.getDrawable(R.drawable.edit_tag_btn_select_style)
                    infantBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    finishBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    finishBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(this, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}