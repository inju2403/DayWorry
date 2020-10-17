package com.inju.dayworry

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.WorryListViewModel
import com.inju.dayworry.worry.worryList.adapter.SearchListAdapter
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@RequiresApi(26)
class SearchActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val statusBarColor = "dark"

    private var searchListViewModel: WorryListViewModel?= null
    private lateinit var listAdapter: SearchListAdapter

    private var searchKeyword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        job = Job()
        searchLoadingUi.visibility = View.GONE
        searchResultText.visibility = View.INVISIBLE
        searchResetImage.visibility = View.GONE

        setViewModel()
        setLayoutManager()
        setKeywordSearch()
        observeViewModel()
        recycleviewBottomDetection()
    }

    private fun setViewModel() {
        searchListViewModel = application!!.let {
            ViewModelProvider(viewModelStore, WorryListInjector(
                this.application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }

    private fun setLayoutManager() {
        searchWorryListView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun setKeywordSearch() {

        searchTextEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchKeyword = s.toString()
                if(s.toString().isNotEmpty()) searchResetImage.visibility = View.VISIBLE
                else searchResetImage.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        searchTextEdit.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // 엔터 눌렀을때 행동
                if(searchKeyword.isEmpty()) {
                    showToast("검색어를 입력해주세요")
                }
                else {
                    searchListViewModel!!.initKeywordSearch(searchKeyword)
                    searchResultText.visibility = View.VISIBLE
                }
            }
            true
        }

        searchResetImage.setOnClickListener {
            searchTextEdit.setText("")
        }

        canselText.setOnClickListener {
            finish()
        }

    }

    private fun observeViewModel() {
        searchListViewModel!!.let {
            it.editSearch.observe(
                this,
                Observer {
                    val intent = Intent(this, WorryDetailActivity::class.java).apply {
                        putExtra("WORRY_ID", it)
                    }
                    startActivity(intent)
                }
            )

            it.search.observe(this,
                Observer {
                    searchWorryListView.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    listAdapter = SearchListAdapter(it, this)

                    listAdapter.event.observe(
                        this,
                        Observer {
                            searchListViewModel!!.handleEvent(it)
                        }
                    )

                    searchWorryListView.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                })

        }
    }

    private fun doPaging() = launch {
        searchLoadingUi.visibility = View.VISIBLE

        searchListViewModel!!.getKeywordSearch(searchKeyword).join()

        searchLoadingUi.visibility = View.GONE
    }

    private fun recycleviewBottomDetection() = launch {
        searchWorryListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    doPaging()
                }

            }
        })
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

    private fun showToast(str: String) {
        var toast = Toast.makeText(this, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }


    override fun onResume() {
        super.onResume()

        setStatusBarColor(statusBarColor)
    }
}