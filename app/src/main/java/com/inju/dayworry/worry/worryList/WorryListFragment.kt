package com.inju.dayworry.worry.worryList

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
        observeViewModel()
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
//        listAdapter = WorryListAdapter()
//        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                requestPagingMovie(query, totalItemsCount + 1)
//            }
//        }
    }

    private fun observeViewModel() {
        worryListViewModel!!.let {
            it.worryListLiveData.observe(viewLifecycleOwner,
                Observer {
                    listAdapter = WorryListAdapter(it)

                    layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    worryListView.layoutManager = layoutManager

                    scrollListener = object : PaginationScrollListener(layoutManager) {
                        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                            requestPaging(query, totalItemsCount + 1)
                            requestPaging()

                        }
                    }

                    worryListView.addOnScrollListener(scrollListener)
                    worryListView.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                }
            )

        }
    }

    private fun requestPaging() {
        worryListViewModel!!.getWorrys()
        Log.d(TAG,"requestPaging")
    }

    override fun onResume() {
        super.onResume()
        listAdapter.notifyDataSetChanged()
        worryListViewModel!!.getWorrys()
//        Log.d(TAG,"리스트: ${worryListViewModel!!.worryListLiveData.value}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        worryListView.adapter = null
    }
}