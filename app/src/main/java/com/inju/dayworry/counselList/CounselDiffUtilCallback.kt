package com.inju.dayworry.counselList

import androidx.recyclerview.widget.DiffUtil
import com.inju.dayworry.model.pojo.Counsel

class CounselDiffUtilCallback: DiffUtil.ItemCallback<Counsel>() {

    override fun areItemsTheSame(oldItem: Counsel, newItem: Counsel): Boolean {
        return oldItem.comment_id == newItem.comment_id
    }

    override fun areContentsTheSame(oldItem: Counsel, newItem: Counsel): Boolean {
        return oldItem.modified_date == newItem.modified_date
    }
}