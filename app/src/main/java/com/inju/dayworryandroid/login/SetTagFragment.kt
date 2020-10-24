package com.inju.dayworryandroid.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inju.dayworryandroid.MainActivity
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.model.pojo.User_REQUEST_POJO
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.utils.Constants.TAG
import kotlinx.android.synthetic.main.fragment_set_profile.nextBtn
import kotlinx.android.synthetic.main.fragment_set_tag.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetTagFragment : Fragment() {

    var litePupleColor = "#9689FC"
    var superLiteGreyColor = "#cbcdd5"
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"

    private val selectTagMinMessage = "고민을 한 가지 이상 선택해주세요"
    private val selectTagMaxMessage = "고민은 3개까지 선택할 수 있어요"

    private val httpCall: ApiService? = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
        ApiService::class.java)

    private var hashTag: MutableList<String> = mutableListOf()
    private var userAgeValue: Int = 0
    private var hashTagString: String = ""
    private var profileImage: String = ""
    private var defaultImage: String = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default_01.jpg"

    private var userId: Long = 0
    private val defaultLong: Long = 0
    private var totalCnt = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_tag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()

        setTagLoadingUi.visibility = View.GONE

        userId = pref.getLong("userId", defaultLong)
        Log.d(TAG,"userId: $userId")

        nextBtn.setOnClickListener {
            if(hashTag.size == 0) {
                showToast(selectTagMinMessage)
            }
            else {
                profileImage = pref.getString("profileImage", defaultImage).toString()
                Log.d(TAG,"프로필 이미지: ${this.profileImage}")
                editor.putString("userName",(activity as SetProfileActivity).userName)
                when((activity as SetProfileActivity).userAge) {
                    "1~9" -> userAgeValue = 1
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
                Log.d(TAG, hashTagString)
                editor.putString("hashTags",hashTagString)
                editor.commit()

                requsetProfileUpdate()
                moveMainActivity()
            }
        }

        setTagBtn()
    }

    private fun requsetProfileUpdate() {
        setTagLoadingUi.visibility = View.VISIBLE
        val userRequestPojo = User_REQUEST_POJO(
            userAgeValue,
            (activity as SetProfileActivity).userName,
            profileImage,
            hashTag,
            userId)

        httpCall?.updateProfile(userRequestPojo)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "updateProfile - onFailed() called / t: ${t}")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when (response.code()) {
                    200 -> {
                        startActivity(Intent(context, MainActivity::class.java))
                        activity!!.finish()
                    }
                    400 -> {
                        Log.d(TAG, "kakaologin - 400 onFailed() called / t: ${response.body()}")
                    }
                }
            }

        })
    }


    private fun setTagBtn() {
        dailyLiftBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dailyLiftBtn.isSelected = false
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "일상") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("일상")
                    dailyLiftBtn.isSelected = true
                    dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        familyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                familyBtn.isSelected = false
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "가족") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("가족")
                    familyBtn.isSelected = true
                    familyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    familyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        friendBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                friendBtn.isSelected = false
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "친구사이") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("친구사이")
                    friendBtn.isSelected = true
                    friendBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    friendBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        dateBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dateBtn.isSelected = false
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "연애") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("연애")
                    dateBtn.isSelected = true
                    dateBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    dateBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        schoolBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                schoolBtn.isSelected = false
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "학교생활") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("학교생활")
                    schoolBtn.isSelected = true
                    schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    schoolBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        jobBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                jobBtn.isSelected = false
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "직장생활") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("직장생활")
                    jobBtn.isSelected = true
                    jobBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    jobBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        employmentBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                employmentBtn.isSelected = false
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "취업") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("취업")
                    employmentBtn.isSelected = true
                    employmentBtn.background =
                        resources.getDrawable(R.drawable.tag_btn_select_style)
                    employmentBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        courseBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                courseBtn.isSelected = false
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "진로") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("진로")
                    courseBtn.isSelected = true
                    courseBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    courseBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        moneyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                moneyBtn.isSelected = false
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "돈") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("돈")
                    moneyBtn.isSelected = true
                    moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    moneyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        healthBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                healthBtn.isSelected = false
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "건강") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("건강")
                    healthBtn.isSelected = true
                    healthBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    healthBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        marriedBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                marriedBtn.isSelected = false
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "기혼자만 아는") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("기혼자만 아는")
                    marriedBtn.isSelected = true
                    marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    marriedBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }

        infantBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                infantBtn.isSelected = false
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }

                for(idx in 0 until hashTag.size) {
                    if(hashTag[idx] == "육아") {
                        hashTag.removeAt(idx)
                        break
                    }
                }
            }
            else {
                if(totalCnt==3) showToast(selectTagMaxMessage)
                else {
                    hashTag.add("육아")
                    infantBtn.isSelected = true
                    infantBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    infantBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }
    }

    private fun moveMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        activity!!.finish()
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity!!, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }

    private fun setStatusBarColor() {
        var view = activity!!.window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(view != null) {
                activity!!.window.statusBarColor = Color.parseColor(Constants.darkNaviColor)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        setStatusBarColor()
    }

}