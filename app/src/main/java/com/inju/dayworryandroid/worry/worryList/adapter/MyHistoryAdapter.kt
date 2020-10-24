package com.inju.dayworryandroid.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.model.pojo.Worry
import com.inju.dayworryandroid.mypage.MyWorryHistoryActivity
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.worry.worryList.WorryItemViewHolder
import com.inju.dayworryandroid.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_worry.view.*

class MyHistoryAdapter(private val list: MutableList<Worry>,
                       activity: MyWorryHistoryActivity,
                       val event: MutableLiveData<WorryListEvent> = MutableLiveData()
) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    private val activity = activity
    val pref = activity.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val supportFragmentManager = activity.supportFragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_worry_history, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        holder.containerView.title.text = list[position].title
        holder.containerView.content.text = list[position].content
        holder.containerView.setOnClickListener {
            event.value = WorryListEvent.OnHistoryItemClick(list[position].postId)
        }
        holder.containerView.worryItemTimeText.text = list[position].createdDate.substring(0..3) + "." + list[position].createdDate.substring(5..6) + "." + list[position].createdDate.substring(8..9)

        holder.containerView.commentCountText.text = list[position].commentNum.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}