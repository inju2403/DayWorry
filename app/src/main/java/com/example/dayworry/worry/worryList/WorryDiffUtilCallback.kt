package com.example.dayworry.worry.worryList

import androidx.recyclerview.widget.DiffUtil
import com.example.dayworry.model.Worry

class WorryDiffUtilCallback: DiffUtil.ItemCallback<Worry>() {

    override fun areItemsTheSame(oldItem: Worry, newItem: Worry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Worry, newItem: Worry): Boolean {
        return oldItem.createdAt == newItem.createdAt
    }
}
