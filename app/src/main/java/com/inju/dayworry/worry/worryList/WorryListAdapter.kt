package com.inju.dayworry.worry.worryList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import kotlinx.android.synthetic.main.item_worry.view.*
import java.text.SimpleDateFormat

class WorryListAdapter(val event: MutableLiveData<WorryListEvent> = MutableLiveData()) : ListAdapter<Worry, WorryItemViewHolder>(
    WorryDiffUtilCallback())
{

    private val timeFormat = SimpleDateFormat("mm : ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worry, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        getItem(position).let {
            var worryId = it?.postId
            holder.containerView.title.text = it.title
            holder.containerView.content.text = it.content
            holder.containerView.setOnClickListener {
                event.value = WorryListEvent.OnWorryItemClick(worryId!!)
            }
            holder.containerView.worryItemTimeText.text = timeFormat.format(it.modifiedDate)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}