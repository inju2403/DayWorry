package com.inju.dayworry.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.utils.Constants.TAG
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import kotlinx.android.synthetic.main.fragment_home1.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@RequiresApi(26)
class HomeFragment1(worry: Worry) : Fragment() {

    private var worry = worry

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUi()

        lookIntoBtn.setOnClickListener {
            Log.d(TAG, "postId: ${worry.postId}")
            val intent = Intent(activity, WorryDetailActivity::class.java).apply {
                putExtra("WORRY_ID", worry.postId)
            }
            startActivity(intent)
        }
    }

    private fun setUi() {
        var imageUrl = worry.postImage
        if(imageUrl != "") Glide.with(this).load(imageUrl).into(worryImage)
        else {
            worryImage.visibility = View.GONE
            line1.visibility = View.GONE
        }

        titleText.text = worry.title
        contentText.text = worry.content
        tagBtn.text = worry.tagName

        when(worry.tagName) {
            "친구사이" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99f, resources.displayMetrics).toInt()
            "직장생활" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99f, resources.displayMetrics).toInt()
            "학교생활" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99f, resources.displayMetrics).toInt()
            "기혼자만 아는" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 132f, resources.displayMetrics).toInt()
            else -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 68f, resources.displayMetrics).toInt()
        }

        //        val currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val currentTime = LocalDateTime.now()
        val worryTime = LocalDateTime.parse(worry.createdDate)

        val day: Long = (60 * 60 * 24).toLong()
        val remainTimeSec = day - ChronoUnit.SECONDS.between(worryTime, currentTime)
        val remainHour = remainTimeSec / 3600
        var remainMinute = ((remainTimeSec/60) - remainHour*60).toString()
        if(remainMinute.length == 1) remainMinute = "0$remainMinute"
        val remainTime = "$remainHour:$remainMinute"

        timeText.text = remainTime

        commentCountText.text = worry.commentNum.toString()
    }
}
