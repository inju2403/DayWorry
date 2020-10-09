package com.inju.dayworry.counselList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Counsel
import kotlinx.android.synthetic.main.item_counsel.view.*

class CounselListAdapter(private val list: MutableList<Counsel>) :
    RecyclerView.Adapter<CounselItemViewHolder> ()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounselItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_counsel, parent, false)
        return CounselItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CounselItemViewHolder, position: Int) {
//        getItem(position).let {
//            var counselId = it?.commentId
//            holder.containerView.counselContent.text = it.content
//            holder.containerView.timeText.text = timeFormat.format(it.modifiedDate)
//            holder.containerView.commentCountText.text = it.commentLikes.toString()
//        }

//        holder.containerView.profileImage. 이미지 받아오기기
        holder.containerView.userNameText.text = list[position].nickname
        holder.containerView.counselContent.text = list[position].content
        holder.containerView.counselItemTimeText.text = list[position].createdDate.substring(11..15)
        holder.containerView.commentCountText.text = list[position].commentLikes.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}