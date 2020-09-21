package com.inju.dayworry.worry.worryList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.utils.Constants.TAG
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.fragment_worry_list.*

class WorryListFragment : Fragment() {

    private var worryListViewModel: WorryListViewModel ?= null

    private lateinit var listAdapter: WorryListAdapter
    private lateinit var scrollListener: PaginationScrollListener
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_worry_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setViewModel()
        setUpAdapter()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()

    }

    private fun setViewModel() {
        worryListViewModel = activity!!.application!!.let {
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
                worryListViewModel!!.handleEvent(it)
            }
        )
        worryListView.adapter = listAdapter
    }

    private fun setLayoutManager() {
        worryListView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
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
                    listAdapter.submitList(it)
                })
        }
    }

    private fun recycleviewBottomDetection() {
        worryListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    worryListViewModel!!.getWorrys()
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()

        // 고민글을 추가하고 다시 고민리스트로 가면 0 페이지부터 다시 부름
//        worryListViewModel!!.InitWorrys()
//        Log.d(TAG,"리스트: ${worryListViewModel!!.worryListLiveData.value}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        worryListView.adapter = null
    }
}