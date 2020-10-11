package com.inju.dayworry.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.EditBottomSheetFragment
import com.inju.dayworry.utils.ReportBottomSheetFragment
import com.inju.dayworry.worry.worryList.WorryItemViewHolder
import com.inju.dayworry.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_worry.view.*
import java.text.SimpleDateFormat

class MyWorryListAdapter(private val list: MutableList<Worry>,
                         activity: MainActivity,
                         val event: MutableLiveData<WorryListEvent> = MutableLiveData()
) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    private val timeFormat = SimpleDateFormat("HH : mm")

    private val activity = activity
    val pref = activity.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val supportFragmentManager = activity.supportFragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_worry_current, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        holder.containerView.title.text = list[position].title
        holder.containerView.content.text = list[position].content
        holder.containerView.setOnClickListener {
            event.value = WorryListEvent.OnMyWorryItemClick(list[position].postId!!)
        }
        holder.containerView.worryItemTimeText.text = list[position].createdDate.substring(11..15)

        holder.containerView.moreImage.setOnClickListener {
            //내 글 수정
            val editBottomSheetFragment = EditBottomSheetFragment(list[position].postId, 2)

            editBottomSheetFragment.show(supportFragmentManager, "editBottomSheetFragment")
        }
        holder.containerView.commentCountText.text = list[position].commentNum.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}