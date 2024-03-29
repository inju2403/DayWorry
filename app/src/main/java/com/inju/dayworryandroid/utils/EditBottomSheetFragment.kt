package com.inju.dayworryandroid.utils

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inju.dayworryandroid.MainActivity
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.worry.worryDetail.AddWorryActivity
import kotlinx.android.synthetic.main.edit_bottomsheet_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class EditBottomSheetFragment(val postId: Long, private val flag: Int) : BottomSheetDialogFragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var userId: String = ""

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

        val pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        userId = pref.getString("userId", "").toString()

        job = Job()

        editLayout.setOnClickListener {
            //게시물 수정
            val intent = Intent(activity!!, AddWorryActivity::class.java).apply {
                putExtra("WORRY_ID", postId)
            }
            startActivity(intent)
            if(flag == 1 ) activity!!.finish() // 고민 상세화면에서 왔을 경우
            dismiss()
        }

        deleteLayout.setOnClickListener {
            //게시물 삭제
            deleteWorry()
        }
    }

    private fun deleteWorry() = launch {
        val httpCall: ApiService?
                = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(ApiService::class.java)
        httpCall?.deleteWorry(postId)
        showToast("고민글이 삭제되었습니다")

        when(flag) {
            0 -> {
                (activity as MainActivity).getWorryListViewModel?.InitWorrys("전체") // 고민 리스트에서 왔을 경우
                (activity as MainActivity).getWorryListViewModel?.getStorys() // 스토리 업데이트
            }
            1 -> {
                activity!!.finish() // 고민 상세화면에서 왔을 경우
            }
            2 -> {
                (activity as MainActivity).getWorryListViewModel?.initMyWorrys(userId) // 마이페이지에서 왔을 경우
            }
        }
        if(flag == 1) activity!!.finish() // 고민 상세화면에서 왔을 경우
        else (activity as MainActivity).getWorryListViewModel?.InitWorrys("전체") // 고민 리스트에서 왔을 경우
        dismiss()
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity!!, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}
