package com.inju.dayworry.worry.worryDetail

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.R
import com.inju.dayworry.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_add_worry.*

class AddWorryActivity : AppCompatActivity() {

    private val RESULT_OK = 101
    private var worryDetailViewModel: WorryDetailViewModel? = null

    private var tag: String? = null

    var litePupleColor = "#9689FC" // 텍스트 색상
    var superLiteGreyColor = "#cbcdd5" // 텍스트 색상

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_worry)

        setSupportActionBar(addWorryActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_toolbar_cancel)

        setViewModel()
        observeViewModel()
        setTextChangeListener()
        setTagBtn()

//        val worryId = intent.getStringExtra("WORRY_ID")
//        if(worryId != null) {
//            worryLoading(worryId)
//        }
    }

    private fun setViewModel() {
        worryDetailViewModel = application!!.let {
            ViewModelProvider(this, WorryDetailInjector(
                this.application
            ).provideWorryDetailViewModelFactory())
                .get(WorryDetailViewModel::class.java)
        }
    }

    private fun observeViewModel() {
        worryDetailViewModel!!.worry.observe (this, Observer {
            titleEdit.setText(it.title)
            contentEdit.setText(it.content)
        })
    }

    private fun setTextChangeListener() {

        titleEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                worryDetailViewModel!!.setWorryTitle(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        contentEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                worryDetailViewModel!!.setWorryContent(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_worry_activity_toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.add_worry_tab -> {
                worryUpdateLoading()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun worryUpdateLoading() {
        worryDetailViewModel!!.addOrUpdateWorry(this)
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun worryLoading(worryId: Long) {
        worryDetailViewModel!!.getWorryById(worryId)
    }

    private fun setTagBtn() {
        dailyLiftBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dailyLiftBtn.isSelected = false
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "일상"
                resetBtnColor()
                dailyLiftBtn.isSelected = true
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        familyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                familyBtn.isSelected = false
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "가족"
                resetBtnColor()
                familyBtn.isSelected = true
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                familyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        friendBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                friendBtn.isSelected = false
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "친구"
                resetBtnColor()
                friendBtn.isSelected = true
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                friendBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        dateBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dateBtn.isSelected = false
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "연애"
                resetBtnColor()
                dateBtn.isSelected = true
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dateBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        schoolBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                schoolBtn.isSelected = false
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "학교"
                resetBtnColor()
                schoolBtn.isSelected = true
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                schoolBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        jobBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                jobBtn.isSelected = false
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "직장"
                resetBtnColor()
                jobBtn.isSelected = true
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                jobBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        employmentBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                employmentBtn.isSelected = false
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "취업"
                resetBtnColor()
                employmentBtn.isSelected = true
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                employmentBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        courseBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                courseBtn.isSelected = false
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "진로"
                resetBtnColor()
                courseBtn.isSelected = true
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                courseBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        moneyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                moneyBtn.isSelected = false
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "돈"
                resetBtnColor()
                moneyBtn.isSelected = true
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                moneyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        healthBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                healthBtn.isSelected = false
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "건강"
                resetBtnColor()
                healthBtn.isSelected = true
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                healthBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        marriedBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                marriedBtn.isSelected = false
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "기혼"
                resetBtnColor()
                marriedBtn.isSelected = true
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                marriedBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        infantBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                infantBtn.isSelected = false
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                tag = "육아"
                resetBtnColor()
                infantBtn.isSelected = true
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                infantBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }
    }

    private fun resetBtnColor() {
        dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))

        dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        familyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        friendBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dateBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        jobBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        courseBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        healthBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)

        dailyLiftBtn.isSelected = false
        familyBtn.isSelected = false
        friendBtn.isSelected = false
        dateBtn.isSelected = false
        schoolBtn.isSelected = false
        jobBtn.isSelected = false
        employmentBtn.isSelected = false
        courseBtn.isSelected = false
        moneyBtn.isSelected = false
        healthBtn.isSelected = false
        marriedBtn.isSelected = false
        infantBtn.isSelected = false
    }
}