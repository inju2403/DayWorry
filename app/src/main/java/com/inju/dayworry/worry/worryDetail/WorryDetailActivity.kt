package com.inju.dayworry.worry.worryDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.text.SimpleDateFormat

class WorryDetailActivity : AppCompatActivity() {

    private var worryDetailViewModel: WorryDetailViewModel? = null
    private var counselListViewModel: CounselListViewModel? = null
    private lateinit var listAdapter: CounselListAdapter

    private val timeFormat = SimpleDateFormat("mm : ss")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worry_detail)

        setViewModel()
        setUpAdapter()
        observeViewModel()
        recycleviewBottomDetection()

        val worryId = intent.getLongExtra("WORRY_ID", -1)
        if(worryId != (-1).toLong()) {
            worryLoading(worryId)
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

    private fun setUpAdapter() {
        listAdapter = CounselListAdapter()
        listAdapter.event.observe(
            this,
            Observer {
                counselListViewModel!!.handleEvent(it)
            }
        )
        commentRecyclerView.adapter = listAdapter
    }

    private fun observeViewModel() {
        worryDetailViewModel!!.worry.observe (this, Observer {
            titleText.text = it.title
            contentText.text = it.content
            timeText.text = timeFormat.format(it.modified_date)
        })

        counselListViewModel!!.let {
            it.counselList.observe(this,
                Observer {
                    listAdapter.submitList(it)
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
//                    counselListViewModel!!.getCounsels()
                }

            }
        })
    }

    private fun worryLoading(worryId: Long) {
        worryDetailViewModel!!.getWorryById(worryId)
    }

    override fun onResume() {
        super.onResume()

        // 상담 댓글을 추가하고 다시 상당글 리스트로 가면 0 페이지부터 다시 부름
//        counselListViewModel!!.InitCounsels()
    }

    override fun onDestroy() {
        super.onDestroy()
        commentRecyclerView.adapter = null
    }
}