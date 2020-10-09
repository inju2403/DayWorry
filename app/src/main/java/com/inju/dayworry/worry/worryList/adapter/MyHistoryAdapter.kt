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

class MyHistoryAdapter(private val list: MutableList<Worry>,
                       val event: MutableLiveData<WorryListEvent> = MutableLiveData()
) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_worry_history, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        holder.containerView.title.text = list[position].title
        holder.containerView.content.text = list[position].content
        holder.containerView.setOnClickListener {
            event.value = WorryListEvent.OnHistoryItemClick(list[position].postId!!)
        }
        holder.containerView.worryItemTimeText.text = list[position].createdDate.substring(0..3) + "." + list[position].createdDate.substring(5..6) + "." + list[position].createdDate.substring(8..9)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}