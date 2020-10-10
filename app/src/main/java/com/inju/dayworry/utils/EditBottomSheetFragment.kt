package com.inju.dayworry.utils

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inju.dayworry.R
import com.inju.dayworry.worry.worryDetail.AddWorryActivity
import kotlinx.android.synthetic.main.edit_bottomsheet_fragment.*


class EditBottomSheetFragment(val postId: Long) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_bottomsheet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)

        editLayout.setOnClickListener {
            //게시물 수정
            val intent = Intent(activity!!, AddWorryActivity::class.java).apply {
                putExtra("WORRY_ID", postId)
            }
            startActivity(intent)
        }

        deleteLayout.setOnClickListener {
            //게시물 삭제
        }
    }
}
