package com.inju.dayworryandroid.counsel.counselList

import androidx.recyclerview.widget.DiffUtil
import com.inju.dayworryandroid.model.pojo.Counsel

class CounselDiffUtilCallback: DiffUtil.ItemCallback<Counsel>() {

    override fun areItemsTheSame(oldItem: Counsel, newItem: Counsel): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: Counsel, newItem: Counsel): Boolean {
        return oldItem.modifiedDate == newItem.modifiedDate
    }
}