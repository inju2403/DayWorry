package com.inju.dayworry.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import kotlinx.android.synthetic.main.fragment_set_profile.nextBtn
import kotlinx.android.synthetic.main.fragment_set_tag.courseBtn
import kotlinx.android.synthetic.main.fragment_set_tag.dailyLiftBtn
import kotlinx.android.synthetic.main.fragment_set_tag.dateBtn
import kotlinx.android.synthetic.main.fragment_set_tag.employmentBtn
import kotlinx.android.synthetic.main.fragment_set_tag.familyBtn
import kotlinx.android.synthetic.main.fragment_set_tag.friendBtn
import kotlinx.android.synthetic.main.fragment_set_tag.healthBtn
import kotlinx.android.synthetic.main.fragment_set_tag.infantBtn
import kotlinx.android.synthetic.main.fragment_set_tag.jobBtn
import kotlinx.android.synthetic.main.fragment_set_tag.marriedBtn
import kotlinx.android.synthetic.main.fragment_set_tag.moneyBtn
import kotlinx.android.synthetic.main.fragment_set_tag.schoolBtn

class SetTagFragment : Fragment() {

    var litePupleColor = "#9689FC" // 텍스트 색상
    var superLiteGreyColor = "#cbcdd5" // 텍스트 색상
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"
    private var hashTag: MutableList<String> = mutableListOf()

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

//        preBtn.setOnClickListener {
//            moveSetProfileFragment()
//        }

        nextBtn.setOnClickListener {
            moveMainActivity()
        }

        setTagBtn()
    }

//    private fun moveSetProfileFragment() {
//        (activity as SetProfileActivity).switchSetProfileFragment()
//    }


    private fun setTagBtn() {
        dailyLiftBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dailyLiftBtn.isSelected = false
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                --totalCnt
                if(totalCnt==0) {
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("일상")
                    dailyLiftBtn.isSelected = true
                    dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("가족")
                    familyBtn.isSelected = true
                    familyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    familyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("친구")
                    friendBtn.isSelected = true
                    friendBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    friendBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("연애")
                    dateBtn.isSelected = true
                    dateBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    dateBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("학교")
                    schoolBtn.isSelected = true
                    schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    schoolBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("직장")
                    jobBtn.isSelected = true
                    jobBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    jobBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("취업")
                    employmentBtn.isSelected = true
                    employmentBtn.background =
                        resources.getDrawable(R.drawable.tag_btn_select_style)
                    employmentBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("진로")
                    courseBtn.isSelected = true
                    courseBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    courseBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("돈")
                    moneyBtn.isSelected = true
                    moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    moneyBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("건강")
                    healthBtn.isSelected = true
                    healthBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    healthBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("기혼")
                    marriedBtn.isSelected = true
                    marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    marriedBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
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
                    nextBtn.setBackgroundColor(Color.parseColor(liteNavyColor))
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }
            else {
                if(totalCnt==3) showToast()
                else {
                    hashTag.add("육아")
                    infantBtn.isSelected = true
                    infantBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                    infantBtn.setTextColor(Color.parseColor(litePupleColor))
                    ++totalCnt
                    nextBtn.setBackgroundColor(Color.parseColor(litePupleColor))
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
            }
        }
    }

    private fun moveMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        activity!!.finish()
    }

    private fun showToast() {
        var toast = Toast.makeText(activity!!, "고민은 3개까지 선택할 수 있어요", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}