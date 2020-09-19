package com.inju.dayworry.worry.worryList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry

class WorryListAdapter(val event: MutableLiveData<WorryListEvent> = MutableLiveData()) : ListAdapter<Worry, ItemViewHolder>(
    WorryDiffUtilCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worry, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getItem(position).let {
            var worryId = it?.post_id
//            holder.containerView.summaryView.text = it.content
            holder.containerView.setOnClickListener {
                event.value = WorryListEvent.OnWorryItemClick(worryId!!)
            }
//            holder.containerView.yearView.text = "'${yearFormat.format(it.createdAt)}"
//            holder.containerView.dateView.text = dateFormat.format(it.createdAt)
//            holder.containerView.dayOfTheWeekView.text = weekdayFormat.format(it.createdAt)
            holder.containerView.tag = it.post_id
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}