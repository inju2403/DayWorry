package com.inju.dayworry.worry.worryDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.R
import com.inju.dayworry.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_worry_detail.*
import java.text.SimpleDateFormat

class WorryDetailActivity : AppCompatActivity() {

    private var worryDetailViewModel: WorryDetailViewModel? = null
    private val timeFormat = SimpleDateFormat("mm : ss")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worry_detail)

        setViewModel()
        observeViewModel()

        val worryId = intent.getLongExtra("WORRY_ID", -1)
        if(worryId != (-1).toLong()) {
            worryLoading(worryId)
        }

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
            titleText.text = it.title
            contentText.text = it.content
            timeText.text = timeFormat.format(it.modified_date)

            //상담글리스트 세팅해야함
        })

    }

    private fun worryLoading(worryId: Long) {
        worryDetailViewModel!!.getWorryById(worryId)
    }
}