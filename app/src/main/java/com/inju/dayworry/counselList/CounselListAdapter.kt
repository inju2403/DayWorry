package com.inju.dayworry.counselList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Counsel
import kotlinx.android.synthetic.main.activity_worry_detail.view.*
import kotlinx.android.synthetic.main.item_counsel.view.*
import java.text.SimpleDateFormat

class CounselListAdapter(val event: MutableLiveData<CounselListEvent> = MutableLiveData()) : ListAdapter<Counsel, CounselItemViewHolder>(
    CounselDiffUtilCallback()
)
{

    private val timeFormat = SimpleDateFormat("mm : ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounselItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_counsel, parent, false)
        return CounselItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CounselItemViewHolder, position: Int) {
        getItem(position).let {
            var counselId = it?.comment_id
            holder.containerView.counselContent.text = it.content
            holder.containerView.timeText.text = timeFormat.format(it.modified_date)
            holder.containerView.commentCountText.text = it.comment_likes.toString()
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}