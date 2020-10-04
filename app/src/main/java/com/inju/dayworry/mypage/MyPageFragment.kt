package com.inju.dayworry.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.WorryListAdapter
import com.inju.dayworry.worry.worryList.WorryListViewModel
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment() {

    private var myWorryListViewModel: WorryListViewModel?= null
    private lateinit var listAdapter: WorryListAdapter
    private var userId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        userId = pref.getLong("userId", 0)

        setViewModel()
        setUpAdapter()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()

        setUpClickListener()
        //아이템이 없을때 사라지도록 변경해야함
        emptyPostLayout.visibility = View.GONE
        newImage.visibility = View.GONE
    }

    private fun setViewModel() {
        myWorryListViewModel = activity!!.application!!.let {
            ViewModelProvider(activity!!.viewModelStore, WorryListInjector(
                requireActivity().application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }

    private fun setUpAdapter() {
        listAdapter = WorryListAdapter()
        listAdapter.event.observe(
            viewLifecycleOwner,
            Observer {
                myWorryListViewModel!!.handleEvent(it)
            }
        )
        myCurrentWorryList.adapter = listAdapter
    }

    private fun setLayoutManager() {
        myCurrentWorryList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun observeViewModel() {
        myWorryListViewModel!!.let {
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
                    listAdapter.submitList(it)
                })
        }
    }

    private fun recycleviewBottomDetection() {
        myCurrentWorryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    myWorryListViewModel!!.getMyWorrys(userId)
                }

            }
        })
    }

    private fun setUpClickListener() {
        profileEditBtn.setOnClickListener {
            //계정 정보 수정
            startActivity(Intent(activity!!, EditUserActivity::class.java))
        }
        storageLayout.setOnClickListener {
            //내 글 보관함
            startActivity(Intent(activity!!, MyWorryHistoryActivity::class.java))
        }
        onOffText.setOnClickListener {
            //푸쉬 알림 켜기/끄기
            if(onOffText.text == "켜기") onOffText.text = "끄기"
            else onOffText.text = "켜기"
        }
        deleteUserLayout.setOnClickListener {
            //계정 삭제
        }
    }

    override fun onResume() {
        super.onResume()

        myWorryListViewModel!!.initMyWorrys(userId)
    }
}