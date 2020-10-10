package com.inju.dayworry.worry.worryDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inju.dayworry.R
import com.inju.dayworry.counsel.counselDetail.CounselDetailViewModel
import com.inju.dayworry.counsel.counselDetail.buildlogic.CounselDetailInjector
import com.inju.dayworry.counsel.counselList.CounselListAdapter
import com.inju.dayworry.counsel.counselList.CounselListViewModel
import com.inju.dayworry.counsel.counselList.buildlogic.CounselListInjector
import com.inju.dayworry.model.pojo.COUNSEL_REQUEST_POJO
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.EditBottomSheetFragment
import com.inju.dayworry.utils.ReportBottomSheetFragment
import com.inju.dayworry.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_add_worry.*
import kotlinx.android.synthetic.main.activity_worry_detail.*
import kotlinx.android.synthetic.main.activity_worry_detail.profileImage
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WorryDetailActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryDetailViewModel: WorryDetailViewModel? = null
    private var counselListViewModel: CounselListViewModel? = null
    private var counselDetailViewModel: CounselDetailViewModel? = null
    private lateinit var listAdapter: CounselListAdapter
    private var worryId: Long? = null
    private var profile_image: String = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default02.jpg"
    private var userId: Long? = null
    private var userName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worry_detail)

        val pref = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE)
        userId = pref.getLong("userId", (1).toLong())
        userName = pref.getString("userName", "")

        detailLoadingUi.visibility = View.GONE
        job = Job()

        setViewModel()
        setProfileImage()
        setTextChangeListener()
        setUpClickListener()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()
    }
    private fun setViewModel() {
        worryDetailViewModel = application!!.let {
            ViewModelProvider(this, WorryDetailInjector(
                this.application
            ).provideWorryDetailViewModelFactory())
                .get(WorryDetailViewModel::class.java)
        }


        counselListViewModel = application!!.let {
            ViewModelProvider(viewModelStore, CounselListInjector(
                this.application
            ).provideCounselListViewModelFactory())
                .get(CounselListViewModel::class.java)
        }

        counselDetailViewModel = application!!.let {
            ViewModelProvider(viewModelStore, CounselDetailInjector(
                this.application
            ).provideCounselDetailViewModelFactory())
                .get(CounselDetailViewModel::class.java)
        }
        counselDetailViewModel?.initCounsel()
    }

    private fun setProfileImage() = launch {
        worryId = intent.getLongExtra("WORRY_ID", -1)
        if(worryId != (-1).toLong()) {
            worryLoading(worryId!!).join()
        }

        var imageUrl = worryDetailViewModel?.worry?.value?.userProfileImage
        Log.d(Constants.TAG, "이미지 url: $imageUrl")
        Glide.with(this@WorryDetailActivity).load(imageUrl).into(profileImage)
    }

    private fun setTextChangeListener() {
        commentEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                counselDetailViewModel!!.setCounselContent(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setUpClickListener() = launch {
        sendImage.setOnClickListener {
            if(counselDetailViewModel?.counsel?.value!!.content.isEmpty()) {
                showToast("댓글을 입력해주세요")
            }
            else {
                sendCounsel()
            }
        }
//        commentEditText.setOnKeyListener { v, keyCode, event ->
//                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    // 엔터 눌렀을때 행동
//                    sendCounsel()
//                }
//                true
//            }

        moreImage.setOnClickListener {
            if(userId == worryDetailViewModel?.worry?.value?.userId) {
                //내 글이면 수정
                val editBottomSheetFragment = EditBottomSheetFragment(worryId!!, 1)

                editBottomSheetFragment.show(supportFragmentManager, "editBottomSheetFragment")
            }
            else {
                //내 글이 아니면 신고
                val reportBottomSheetFragment = ReportBottomSheetFragment(worryId!!, 1)

                reportBottomSheetFragment.show(supportFragmentManager, "reportBottomSheetFragment")
            }
        }
    }

    private fun sendCounsel() = launch {
        detailLoadingUi.visibility = View.VISIBLE

        counselDetailViewModel?.addCounsel(
            counselDetailViewModel?.counsel?.value!!.content,
            userId!!,
            worryDetailViewModel?.worry?.value!!.postId,
            profile_image!!,
            userName!!
        )?.join()

        counselListViewModel?.InitCounsels(worryId!!)?.join()

        commentEditText.text.clear()
        detailLoadingUi.visibility = View.GONE
    }

    private fun setLayoutManager() {
        commentRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun observeViewModel() {
        worryDetailViewModel!!.worry.observe (this, Observer {
            titleText.text = it.title
            contentText.text = it.content
            timeText.text = it.createdDate.substring(11..15)
            tagBtn.text = it.tagName
        })

        counselListViewModel!!.let {
            it.counselList.observe(this,
                Observer {
                    commentRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    listAdapter =
                        CounselListAdapter(it, this)
                    commentRecyclerView.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()

                })
        }
    }

    private fun recycleviewBottomDetection() {
        commentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    counselListViewModel!!.getCounsels(worryId!!)
                }

            }
        })
    }

    private fun worryLoading(worryId: Long) = launch {
        detailLoadingUi.visibility = View.VISIBLE
        titleText.visibility = View.GONE
        timeTagLayout.visibility = View.GONE
        contentText.visibility = View.GONE
        commentRecyclerView.visibility = View.GONE

        worryDetailViewModel!!.getWorryById(worryId).join()
        counselListViewModel!!.InitCounsels(worryId).join()

        detailLoadingUi.visibility = View.GONE
        titleText.visibility = View.VISIBLE
        timeTagLayout.visibility = View.VISIBLE
        contentText.visibility = View.VISIBLE
        commentRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        commentRecyclerView.adapter = null
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(this, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}