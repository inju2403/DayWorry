package com.example.dayworry.worry.worryList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dayworry.R
import com.example.dayworry.model.Worry
import kotlinx.android.synthetic.main.item_worry.view.*
import java.util.*

class WorryListAdapter
    : ListAdapter<Worry, ItemViewHolder>(WorryDiffUtilCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worry, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getItem(position).let {
            var worryId = it?.id
            holder.containerView.titleText.text = it.title
            holder.containerView.contentText.text = it.content
        }
    }
}