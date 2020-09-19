package com.inju.dayworry.worry.worryList

import androidx.recyclerview.widget.DiffUtil
import com.inju.dayworry.model.pojo.Worry

class WorryDiffUtilCallback: DiffUtil.ItemCallback<Worry>() {

    override fun areItemsTheSame(oldItem: Worry, newItem: Worry): Boolean {
        return oldItem.post_id == newItem.post_id
    }

    override fun areContentsTheSame(oldItem: Worry, newItem: Worry): Boolean {
        return oldItem.user_id == newItem.user_id
    }
}
