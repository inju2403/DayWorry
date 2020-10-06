package com.inju.dayworry.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.worry.worryList.WorryItemViewHolder
import com.inju.dayworry.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_worry.view.*
import java.text.SimpleDateFormat

class WorryListAdapter(private val list: MutableList<Worry>,
                       val event: MutableLiveData<WorryListEvent> = MutableLiveData()) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    private val timeFormat = SimpleDateFormat("HH : mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worry, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
//        getItem(position).let {
//            var worryId = it?.postId
//            holder.containerView.title.text = it.title
//            holder.containerView.content.text = it.content
//            holder.containerView.setOnClickListener {
//                event.value = WorryListEvent.OnWorryItemClick(worryId!!)
//            }
////            holder.containerView.worryItemTimeText.text = timeFormat.format(it.modifiedDate)
//            holder.containerView.worryItemTimeText.text = it.createdDate.substring(11..15)
//        }
        holder.containerView.title.text = list[position].title
        holder.containerView.content.text = list[position].content
        holder.containerView.setOnClickListener {
            event.value =
                WorryListEvent.OnWorryItemClick(
                    list[position].postId!!
                )
        }
        holder.containerView.worryItemTimeText.text = list[position].createdDate.substring(11..15)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}