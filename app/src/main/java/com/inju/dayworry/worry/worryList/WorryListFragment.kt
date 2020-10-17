package com.inju.dayworry.worry.worryList

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.SearchActivity
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.adapter.StoryAdapter
import com.inju.dayworry.worry.worryList.adapter.WorryListAdapter
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.fragment_worry_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@RequiresApi(26)
class WorryListFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryListViewModel: WorryListViewModel ?= null

    private lateinit var listAdapter: WorryListAdapter
    private lateinit var storyAdapter: StoryAdapter

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
        searchClickLayout.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
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
            it.editStory.observe(
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

                    listAdapter = WorryListAdapter(it, (activity as MainActivity))

                    listAdapter.event.observe(
                        viewLifecycleOwner,
                        Observer {
                            worryListViewModel!!.handleEvent(it)
                        }
                    )

                    worryListView.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                })

            it.story.observe(viewLifecycleOwner,
                Observer {
                    storyListView.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

                    storyAdapter = StoryAdapter(it, (activity as MainActivity))

                    storyAdapter.event.observe(
                        viewLifecycleOwner,
                        Observer {
                            worryListViewModel!!.handleEvent(it)
                        }
                    )

                    storyListView.adapter = storyAdapter
                    storyAdapter.notifyDataSetChanged()
                })

        }
    }

    private fun doPaging() = launch {
        pagingLoadingUi.visibility = View.VISIBLE

        worryListViewModel!!.getWorrys(hashTag).join()

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
                    doPaging()
                }

            }
        })
    }

    private fun initWorrys() = launch {
        worryListLoadingUi.visibility = View.VISIBLE

        worryListViewModel!!.InitWorrys(hashTag).join()
        worryListViewModel!!.getStorys().join()
        listAdapter.notifyDataSetChanged()

        worryListLoadingUi.visibility = View.GONE
    }

    private fun setTagBtn() = launch {
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

    override fun onResume() {
        super.onResume()

        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
        initWorrys()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        worryListView.adapter = null
    }
}