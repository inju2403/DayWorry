package com.inju.dayworryandroid.mypage

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.worry.worryList.adapter.MyHistoryAdapter
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.worry.worryDetail.WorryDetailActivity
import com.inju.dayworryandroid.worry.worryList.WorryListViewModel
import com.inju.dayworryandroid.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.activity_my_worry_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyWorryHistoryActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryListViewModel: WorryListViewModel?= null
    val getWorryListViewModel get() = worryListViewModel

    private lateinit var listAdapter: MyHistoryAdapter
    private var userId: String = ""
    private var defaultLong: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_worry_history)

        val pref = getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        userId = pref.getString("userId", "").toString()

        job = Job()

        setViewModel()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()

        //아이템이 없을때 사라지도록 변경
        historyPagingLoadingUi.visibility = View.GONE
    }

    private fun setViewModel() {
        worryListViewModel = application!!.let {
            ViewModelProvider(viewModelStore, WorryListInjector(
                this.application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }

    private fun setLayoutManager() {
        myWorryHistoryView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun observeViewModel() {
        worryListViewModel!!.let {
            it.editHistory.observe(
                this,
                Observer {
                    val intent = Intent(this, WorryDetailActivity::class.java).apply {
                        putExtra("WORRY_ID", it)
                    }
                    startActivity(intent)
                }
            )
            it.myHistory.observe(this,
                Observer {
                    myWorryHistoryView.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    listAdapter = MyHistoryAdapter(it, this)

                    listAdapter.event.observe(
                        this,
                        Observer {
                            worryListViewModel!!.handleEvent(it)
                        }
                    )

                    myWorryHistoryView.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun paging() = launch {
        historyPagingLoadingUi.visibility = View.VISIBLE

        worryListViewModel!!.getHistory(userId).join()
        listAdapter.notifyDataSetChanged()

        historyPagingLoadingUi.visibility = View.GONE
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
                    paging()
                }

            }
        })
    }

    private fun initMyWorrys() = launch {
        historyInitLoadingUi.visibility = View.VISIBLE

        worryListViewModel!!.initHistory(userId).join()
        listAdapter.notifyDataSetChanged()

        historyInitLoadingUi.visibility = View.GONE
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

    override fun onResume() {
        super.onResume()
        initMyWorrys()
        setStatusBarColor("dark")
    }

    override fun onDestroy() {
        super.onDestroy()
        myWorryHistoryView.adapter = null
    }
}