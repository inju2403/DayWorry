package com.inju.dayworry.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inju.dayworry.R
import com.inju.dayworry.login.SetProfileActivity
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpClickListener()
    }

    private fun setUpClickListener() {
        profileEditBtn.setOnClickListener {
            //계정 정보 수정
            startActivity(Intent(activity!!, EditUserActivity::class.java))
        }
        storageLayout.setOnClickListener {
            //내 글 보관함
            startActivity(Intent(activity!!, MyWorryHistoryActivity::class.java))
        }
        onOffText.setOnClickListener {
            //푸쉬 알림 켜기/끄기
            if(onOffText.text == "켜기") onOffText.text = "끄기"
            else onOffText.text = "켜기"
        }
        deleteUserLayout.setOnClickListener {
            //계정 삭제
        }
    }
}