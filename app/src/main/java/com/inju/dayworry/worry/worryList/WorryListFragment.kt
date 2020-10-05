package com.inju.dayworry.worry.worryList

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.utils.Constants.TAG
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.activity_add_worry.*
import kotlinx.android.synthetic.main.fragment_worry_list.*
import kotlinx.android.synthetic.main.fragment_worry_list.courseBtn
import kotlinx.android.synthetic.main.fragment_worry_list.dailyLiftBtn
import kotlinx.android.synthetic.main.fragment_worry_list.dateBtn
import kotlinx.android.synthetic.main.fragment_worry_list.employmentBtn
import kotlinx.android.synthetic.main.fragment_worry_list.familyBtn
import kotlinx.android.synthetic.main.fragment_worry_list.friendBtn
import kotlinx.android.synthetic.main.fragment_worry_list.healthBtn
import kotlinx.android.synthetic.main.fragment_worry_list.infantBtn
import kotlinx.android.synthetic.main.fragment_worry_list.jobBtn
import kotlinx.android.synthetic.main.fragment_worry_list.marriedBtn
import kotlinx.android.synthetic.main.fragment_worry_list.moneyBtn
import kotlinx.android.synthetic.main.fragment_worry_list.schoolBtn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WorryListFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryListViewModel: WorryListViewModel ?= null

    private lateinit var listAdapter: WorryListAdapter
    private lateinit var scrollListener: PaginationScrollListener
    private lateinit var layoutManager: LinearLayoutManager

    private var sort: String = "created_at" //정렬 기준 created_at or hits

    var litePupleColor = "#9689FC" // 텍스트 색상
    var superLiteGreyColor = "#cbcdd5" // 텍스트 색상

    private var hashTag: String = "전체"
    private var searchKeyword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_worry_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        worryListLoadingUi.visibility = View.GONE
        pagingLoadingUi.visibility = View.GONE

        job = Job()

        setViewModel()
        setLayoutManager()
        setKeywordSearch()
        observeViewModel()
        recycleviewBottomDetection()
        setTagBtn()
    }

    private fun setViewModel() {
        worryListViewModel = activity!!.application!!.let {
            ViewModelProvider(activity!!.viewModelStore, WorryListInjector(
                requireActivity().application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }

    private fun setLayoutManager() {
        worryListView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun setKeywordSearch() {
        searchTextEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchKeyword = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        searchTextEdit.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                // 엔터 눌렀을때 행동
                worryListViewModel!!.initKeywordSearch(searchKeyword)
            }
            true
        }

        searchImage.setOnClickListener {
            worryListViewModel!!.initKeywordSearch(searchKeyword)
        }
    }

    private fun observeViewModel() {
        worryListViewModel!!.let {
            it.editWorry.observe(
                viewLifecycleOwner,
                Observer {
                    val intent = Intent(activity, WorryDetailActivity::class.java).apply {
                        putExtra("WORRY_ID", it)
                    }
                    startActivity(intent)
                }
            )
            it.worryList.observe(viewLifecycleOwner,
                Observer {
                    worryListView.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

                    listAdapter = WorryListAdapter(it)

                    listAdapter.event.observe(
                        viewLifecycleOwner,
                        Observer {
                            worryListViewModel!!.handleEvent(it)
                        }
                    )

                    worryListView.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                })

        }
    }

    private fun judgeTagOrSearch() = launch {
        pagingLoadingUi.visibility = View.VISIBLE

        if(worryListViewModel!!.getWorrysState()) worryListViewModel!!.getWorrys(hashTag).join()
        else worryListViewModel!!.getKeywordSearch(hashTag).join()
        listAdapter.notifyDataSetChanged()

        pagingLoadingUi.visibility = View.GONE
    }

    private fun recycleviewBottomDetection() = launch {
        worryListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    judgeTagOrSearch()
                }

            }
        })
    }

    private fun initWorrys() = launch {
        worryListLoadingUi.visibility = View.VISIBLE

        worryListViewModel!!.InitWorrys(hashTag).join()
        listAdapter.notifyDataSetChanged()

        worryListLoadingUi.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
        initWorrys()
//        Log.d(TAG,"리스트: ${worryListViewModel!!.worryList.value}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        worryListView.adapter = null
    }

    private fun setTagBtn() {
        wholeBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "전체"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                wholeBtn.isSelected = true
                wholeBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                wholeBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        dailyLiftBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "일상"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                dailyLiftBtn.isSelected = true
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        familyBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "가족"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                familyBtn.isSelected = true
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                familyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        friendBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "친구"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                friendBtn.isSelected = true
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                friendBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        dateBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "연애"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                dateBtn.isSelected = true
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dateBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        schoolBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "학교"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                schoolBtn.isSelected = true
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                schoolBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        jobBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "직장"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                jobBtn.isSelected = true
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                jobBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        employmentBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "취업"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                employmentBtn.isSelected = true
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                employmentBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        courseBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "진로"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                courseBtn.isSelected = true
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                courseBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        moneyBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "돈"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                moneyBtn.isSelected = true
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                moneyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        healthBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "건강"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                healthBtn.isSelected = true
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                healthBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        marriedBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "기혼"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                marriedBtn.isSelected = true
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                marriedBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        infantBtn.setOnClickListener {
            if(!it.isSelected) {
                hashTag = "육아"

                worryListLoadingUi.visibility = View.VISIBLE
                worryListViewModel!!.InitWorrys(hashTag)
                worryListLoadingUi.visibility = View.GONE

                resetBtnColor()
                infantBtn.isSelected = true
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                infantBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }
    }

    private fun resetBtnColor() {
        wholeBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))

        wholeBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        familyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        friendBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dateBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        jobBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        courseBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        healthBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)

        wholeBtn.isSelected = false
        dailyLiftBtn.isSelected = false
        familyBtn.isSelected = false
        friendBtn.isSelected = false
        dateBtn.isSelected = false
        schoolBtn.isSelected = false
        jobBtn.isSelected = false
        employmentBtn.isSelected = false
        courseBtn.isSelected = false
        moneyBtn.isSelected = false
        healthBtn.isSelected = false
        marriedBtn.isSelected = false
        infantBtn.isSelected = false
    }

}