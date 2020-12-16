package com.inju.dayworryandroid.worry.worryDetail

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.counsel.counselDetail.CounselDetailViewModel
import com.inju.dayworryandroid.counsel.counselDetail.buildlogic.CounselDetailInjector
import com.inju.dayworryandroid.counsel.counselList.CounselListAdapter
import com.inju.dayworryandroid.counsel.counselList.CounselListViewModel
import com.inju.dayworryandroid.counsel.counselList.buildlogic.CounselListInjector
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.utils.Constants.API_BASE_URL
import com.inju.dayworryandroid.utils.EditBottomSheetFragment
import com.inju.dayworryandroid.utils.ReportBottomSheetFragment
import com.inju.dayworryandroid.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_worry_detail.*
import kotlinx.android.synthetic.main.activity_worry_detail.contentText
import kotlinx.android.synthetic.main.activity_worry_detail.profileImage
import kotlinx.android.synthetic.main.activity_worry_detail.tagBtn
import kotlinx.android.synthetic.main.activity_worry_detail.timeText
import kotlinx.android.synthetic.main.activity_worry_detail.titleText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.coroutines.CoroutineContext

@RequiresApi(26)
class WorryDetailActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryDetailViewModel: WorryDetailViewModel? = null
    private var counselListViewModel: CounselListViewModel? = null
    private var counselDetailViewModel: CounselDetailViewModel? = null
    private lateinit var listAdapter: CounselListAdapter
    private var worryId: Long? = null
    private var profile_image: String = "http://15.165.183.122/default_01.jpg"
    private var profile_image_default: String = "http://15.165.183.122/default_01.jpg"
    private var userId: String = ""
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worry_detail)

        val pref = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE)
        userId = pref.getString("userId", "").toString()
        Log.d("로그그", "$userId")
        userName = pref.getString("userName", "")
        profile_image = pref.getString("profileImage", profile_image_default).toString()

        visibleGone()
        setStatusBarColor("dark")

        job = Job()

        setViewModel()
        setImage()
        setTextChangeListener()
        setUpClickListener()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()
    }

    private fun visibleGone() {
        titleText.visibility = View.GONE
        timeTagLayout.visibility = View.GONE
        contentText.visibility = View.GONE
        commentRecyclerView.visibility = View.GONE
        detailLoadingUi.visibility = View.GONE
        postImageView.visibility = View.GONE
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

    private fun setImage() = launch {
        worryId = intent.getLongExtra("WORRY_ID", -1)
        if(worryId != (-1).toLong()) {
            worryLoading(worryId!!)
        }
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
            if(userName == "") {
                showToast("로그인 후에 이용해주세요")
            }
            else if(counselDetailViewModel?.counsel?.value!!.content.isEmpty()) {
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
            profile_image,
            userName!!
        )?.join()

        counselListViewModel?.InitCounsels(worryId!!, userId!!)?.join()

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

            //        val currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            val currentTime = LocalDateTime.now()
            val worryTime = LocalDateTime.parse(it.createdDate)

            val day: Long = (60 * 60 * 24).toLong()
            val remainTimeSec = day - ChronoUnit.SECONDS.between(worryTime, currentTime)
            val remainHour = remainTimeSec / 3600
            var remainMinute = ((remainTimeSec/60) - remainHour*60).toString()
            if(remainMinute.length == 1) remainMinute = "0$remainMinute"
            val remainTime = "$remainHour:$remainMinute"
            timeText.text = remainTime

            userNameText.text = it.userNickname
            commentNumText.text = it.commentNum.toString()
            tagBtn.text = it.tagName

        })

        counselListViewModel!!.let {
            it.counselList.observe(this,
                Observer {
                    commentRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    listAdapter =
                        CounselListAdapter(it, this, worryDetailViewModel?.worry?.value?.userId!!)

                    listAdapter.event.observe(
                        this,
                        Observer {
                            counselListViewModel!!.handleEvent(it)
                        }
                    )

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
                    counselListViewModel!!.getCounsels(worryId!!, userId!!)
                }

            }
        })
    }

    private fun setTagBtnSize() {
        Log.d(Constants.TAG, "태그 이름: "+ worryDetailViewModel?.worry?.value?.tagName)
        when(worryDetailViewModel?.worry?.value?.tagName) {
            "친구사이" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 89f, resources.displayMetrics).toInt()
            "직장생활" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 89f, resources.displayMetrics).toInt()
            "학교생활" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 89f, resources.displayMetrics).toInt()
            "기혼자만 아는" -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 119f, resources.displayMetrics).toInt()
            else -> tagBtn.layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics).toInt()
        }
    }

    private fun worryLoading(worryId: Long) = launch {
        detailLoadingUi.visibility = View.VISIBLE

        worryDetailViewModel!!.getWorryById(worryId).join()
        counselListViewModel!!.InitCounsels(worryId, userId!!).join()

        var profileImageUrl = worryDetailViewModel?.worry?.value?.userProfileImage
        Log.d(Constants.TAG, "이미지 url: $profileImageUrl")
        Glide.with(this@WorryDetailActivity)
            .load(profileImageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
            .into(profileImage)

        var postImageUrl = API_BASE_URL + worryDetailViewModel?.worry?.value?.postImage
        if(postImageUrl != "") {
            Glide.with(this@WorryDetailActivity).load(postImageUrl).into(postImageView)
            postImageView.visibility = View.VISIBLE
        }

        setTagBtnSize()

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

    private fun setStatusBarColor(str: String) {
        var view = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(view != null) {
                when(str) {
                    "main" -> window.statusBarColor = Color.parseColor(Constants.mainNaviColor)
                    "dark" -> window.statusBarColor = Color.parseColor(Constants.darkNaviColor)
                }
            }
        }
    }
}