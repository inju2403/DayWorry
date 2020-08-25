package com.inju.dayworry.worry.worryList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.model.Worry
import kotlinx.android.synthetic.main.item_worry.view.*

class WorryListAdapter(private val list: MutableList<Worry>)
//    : ListAdapter<Worry, ItemViewHolder>(WorryDiffUtilCallback())
    : RecyclerView.Adapter<ItemViewHolder> ()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worry, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        getItem(position).let {
//            var worryId = it?.id
//            holder.containerView.titleText.text = it.title
//            holder.containerView.contentText.text = it.content
//        }
        holder.containerView.titleText.text = list[position].title
        holder.containerView.contentText.text = list[position].content
    }

    override fun getItemCount(): Int {
        return list.size
    }
}