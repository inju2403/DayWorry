package com.inju.dayworry.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.inju.dayworry.R
import com.inju.dayworry.home.viewPager.DepthPageTransformer
import com.inju.dayworry.home.viewPager.ZoomOutPageTransformer
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.Constants.NUM_PAGES
import com.inju.dayworry.utils.Constants.TAG
import com.inju.dayworry.worry.worryList.WorryListViewModel
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import kotlinx.android.synthetic.main.fragment_home_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@RequiresApi(21)
class HomeListFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryListViewModel: WorryListViewModel?= null
    private lateinit var viewPager: ViewPager2

    private var worryList = mutableListOf<Worry>()

    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        job = Job()
        val pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        userName = pref.getString("userName", "").toString()

        explainSubText.text = "$userName 님이\n공감할 고민이 있어요!"

        setHomeComponents()

    }

    private fun setHomeComponents() = launch {
        homeLoadingUi.visibility = View.VISIBLE

        setViewModel().join()
        setViewPager()

        homeLoadingUi.visibility = View.GONE
    }

    private fun setViewModel() = launch {
        worryListViewModel = activity!!.application!!.let {
            ViewModelProvider(activity!!.viewModelStore, WorryListInjector(
                requireActivity().application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }

        worryListViewModel?.getMainWorrys()?.join()
        worryList = worryListViewModel?.mainWorryList?.value!!
        Log.d(TAG, "main worrylist: $worryList")
    }

    private fun setViewPager() = launch {
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = activity!!.findViewById(R.id.counsel_list_view_pager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(activity!!)
        viewPager.adapter = pagerAdapter

//        viewPager.setPageTransformer(ZoomOutPageTransformer())
        viewPager.setPageTransformer(DepthPageTransformer())

        counsel_list_view_pager.apply {
            adapter = viewPager.adapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            dots_indicator.setViewPager2(viewPager)
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

//        override fun createFragment(position: Int): Fragment = CounselFragment1()
        override fun createFragment(position: Int): Fragment {
            lateinit var frag: Fragment
            when(position) {
                0 -> frag = HomeFragment1(worryList[0])
                1 -> frag = HomeFragment2(worryList[1])
                2 -> frag = HomeFragment3(worryList[2])
            }
            return frag
        }
    }

    override fun onResume() {
        super.onResume()

        setHomeComponents()
    }
}