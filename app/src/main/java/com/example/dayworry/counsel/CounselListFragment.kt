package com.example.dayworry.counsel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.dayworry.R
import com.example.dayworry.counsel.counselList.CounselFragment1
import com.example.dayworry.counsel.counselList.CounselFragment2
import com.example.dayworry.counsel.counselList.CounselFragment3
import com.example.dayworry.counsel.counselList.viewPager.DepthPageTransformer
import com.example.dayworry.counsel.counselList.viewPager.ZoomOutPageTransformer
import com.example.dayworry.utils.Constants.NUM_PAGES
import kotlinx.android.synthetic.main.fragment_counsel_list.*

@RequiresApi(21)
class CounselListFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_counsel_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                0 -> frag = CounselFragment1()
                1 -> frag = CounselFragment2()
                2 -> frag = CounselFragment3()
            }
            return frag
        }
    }
}