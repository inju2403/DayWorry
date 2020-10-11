package com.inju.dayworry.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home3.*
import kotlinx.android.synthetic.main.fragment_home3.commentCountText
import kotlinx.android.synthetic.main.fragment_home3.contentText
import kotlinx.android.synthetic.main.fragment_home3.lookIntoBtn
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
        Glide.with(this).load(imageUrl).into(worryImage)

        titleText.text = worry.title
        contentText.text = worry.content

        timeText.text = worry.createdDate.substring(11..15)
        commentCountText.text = worry.commentNum.toString()
    }

}
