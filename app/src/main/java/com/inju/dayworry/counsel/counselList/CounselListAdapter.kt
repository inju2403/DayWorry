package com.inju.dayworry.counsel.counselList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Counsel
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import kotlinx.android.synthetic.main.activity_worry_detail.*
import kotlinx.android.synthetic.main.item_counsel.view.*

class CounselListAdapter(private val list: MutableList<Counsel>,
                        activity: WorryDetailActivity) :
    RecyclerView.Adapter<CounselItemViewHolder> ()
{

    private var activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounselItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_counsel, parent, false)
        return CounselItemViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: CounselItemViewHolder, position: Int) {
//        getItem(position).let {
//            var counselId = it?.commentId
//            holder.containerView.counselContent.text = it.content
//            holder.containerView.timeText.text = timeFormat.format(it.modifiedDate)
//            holder.containerView.commentCountText.text = it.commentLikes.toString()
//        }
        val imageUrl = list[position].profileImage
        Glide.with(activity).load(imageUrl).into(holder.containerView.profileImage)
        holder.containerView.userNameText.text = list[position].nickname
        holder.containerView.counselContent.text = list[position].content
        holder.containerView.counselItemTimeText.text = list[position].createdDate.substring(11..15)
        holder.containerView.commentCountText.text = list[position].commentLikes.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}