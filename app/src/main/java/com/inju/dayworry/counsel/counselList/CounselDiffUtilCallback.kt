package com.inju.dayworry.counsel.counselList

import androidx.recyclerview.widget.DiffUtil
import com.inju.dayworry.model.pojo.Counsel

class CounselDiffUtilCallback: DiffUtil.ItemCallback<Counsel>() {

    override fun areItemsTheSame(oldItem: Counsel, newItem: Counsel): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: Counsel, newItem: Counsel): Boolean {
        return oldItem.modifiedDate == newItem.modifiedDate
    }
}