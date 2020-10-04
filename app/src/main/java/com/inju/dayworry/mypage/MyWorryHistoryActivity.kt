package com.inju.dayworry.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.WorryListAdapter
import com.inju.dayworry.worry.worryList.WorryListViewModel
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.activity_my_worry_history.*

class MyWorryHistoryActivity : AppCompatActivity() {

    private var myHistoryViewModel: WorryListViewModel?= null
    private lateinit var listAdapter: WorryListAdapter
    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_worry_history)

        setViewModel()
        setUpAdapter()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()
    }

    private fun setViewModel() {
        myHistoryViewModel = application!!.let {
            ViewModelProvider(viewModelStore, WorryListInjector(
                this.application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }

    private fun setUpAdapter() {
        listAdapter = WorryListAdapter()
        listAdapter.event.observe(
            this,
            Observer {
                myHistoryViewModel!!.handleEvent(it)
            }
        )
        myWorryHistoryView.adapter = listAdapter
    }

    private fun setLayoutManager() {
        myWorryHistoryView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun observeViewModel() {
        myHistoryViewModel!!.let {
            it.editWorry.observe(
                this,
                Observer {
                    val intent = Intent(this, WorryDetailActivity::class.java).apply {
                        putExtra("WORRY_ID", it)
                    }
                    startActivity(intent)
                }
            )
            it.worryList.observe(this,
                Observer {
                    listAdapter.submitList(it)
                })
        }
    }

    private fun recycleviewBottomDetection() {
        myWorryHistoryView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    myHistoryViewModel!!.getMyWorrys(userId)
                }

            }
        })
    }
}