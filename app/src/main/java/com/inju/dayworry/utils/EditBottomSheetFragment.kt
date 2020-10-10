package com.inju.dayworry.utils

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.worry.worryDetail.AddWorryActivity
import kotlinx.android.synthetic.main.edit_bottomsheet_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class EditBottomSheetFragment(val postId: Long) : BottomSheetDialogFragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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

        job = Job()

        editLayout.setOnClickListener {
            //게시물 수정
            val intent = Intent(activity!!, AddWorryActivity::class.java).apply {
                putExtra("WORRY_ID", postId)
            }
            startActivity(intent)
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
        (activity as MainActivity).getWorryListViewModel?.InitWorrys("전체")
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity!!, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}
