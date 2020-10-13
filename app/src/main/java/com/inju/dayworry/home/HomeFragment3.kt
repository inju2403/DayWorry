package com.inju.dayworry.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home3.*
import kotlinx.android.synthetic.main.fragment_home3.commentCountText
import kotlinx.android.synthetic.main.fragment_home3.contentText
import kotlinx.android.synthetic.main.fragment_home3.line1
import kotlinx.android.synthetic.main.fragment_home3.lookIntoBtn
import kotlinx.android.synthetic.main.fragment_home3.tagBtn
import kotlinx.android.synthetic.main.fragment_home3.timeText
import kotlinx.android.synthetic.main.fragment_home3.titleText
import kotlinx.android.synthetic.main.fragment_home3.worryImage

class HomeFragment3(worry: Worry) : Fragment() {

    private var worry = worry

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home3, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUi()

        lookIntoBtn.setOnClickListener {
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

//        when(worry.tagName) {
//            "친구사이" -> tagBtn.layoutParams = ConstraintLayout.LayoutParams(99, 40)
//            "직장생활" -> tagBtn.layoutParams = ConstraintLayout.LayoutParams(99, 40)
//            "학교생활" -> tagBtn.layoutParams = ConstraintLayout.LayoutParams(99, 40)
//            "기혼자만 아는" -> tagBtn.layoutParams = ConstraintLayout.LayoutParams(132, 40)
//            else -> tagBtn.layoutParams = ConstraintLayout.LayoutParams(68, 40)
//        }

        timeText.text = worry.createdDate.substring(11..15)
        commentCountText.text = worry.commentNum.toString()
    }

}
