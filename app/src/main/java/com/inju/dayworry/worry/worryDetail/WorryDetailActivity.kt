package com.inju.dayworry.worry.worryDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.counselList.CounselListAdapter
import com.inju.dayworry.counselList.CounselListViewModel
import com.inju.dayworry.counselList.buildlogic.CounselListInjector
import com.inju.dayworry.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_worry_detail.*
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
    private lateinit var listAdapter: CounselListAdapter
    private var worryId: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worry_detail)

        detailLoadingUi.visibility = View.GONE
        job = Job()

        setViewModel()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()

        worryId = intent.getLongExtra("WORRY_ID", -1)
        if(worryId != (-1).toLong()) {
            worryLoading(worryId!!)
        }

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

                    listAdapter = CounselListAdapter(it)
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
        timeText.visibility = View.GONE
        tagBtn.visibility = View.GONE
        contentText.visibility = View.GONE
        timeImage.visibility = View.GONE

        worryDetailViewModel!!.getWorryById(worryId).join()
        counselListViewModel!!.InitCounsels(worryId).join()

        detailLoadingUi.visibility = View.GONE
        titleText.visibility = View.VISIBLE
        timeText.visibility = View.VISIBLE
        tagBtn.visibility = View.VISIBLE
        contentText.visibility = View.VISIBLE
        timeImage.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        // 상담 댓글을 추가하고 다시 상당글 리스트로 가면 0 페이지부터 다시 부름
        counselListViewModel!!.InitCounsels(worryId!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        commentRecyclerView.adapter = null
    }
}