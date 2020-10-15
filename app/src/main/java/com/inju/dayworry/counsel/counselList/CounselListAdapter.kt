package com.inju.dayworry.counsel.counselList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Counsel
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_counsel.view.*

class CounselListAdapter(private val list: MutableList<Counsel>,
                        activity: WorryDetailActivity,
                         val event: MutableLiveData<CounselListEvent> = MutableLiveData()
) :
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
        Glide.with(activity).load(imageUrl)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
                            .into(holder.containerView.profileImage)

        holder.containerView.userNameText.text = list[position].nickname
        holder.containerView.counselContent.text = list[position].content

        var createdTime = list[position].createdDate.substring(11..15)
        if(createdTime[0] == '0' || createdTime.substring(0..2).toInt() < 12) {
            if(createdTime[0] == '0') createdTime = createdTime.substring(1..4)
            createdTime = "AM $createdTime"
        }
        else {
            val createdTimeHour = createdTime.substring(0..12).toInt() - 12
            createdTime = "PM " + createdTimeHour.toString() + createdTime.substring(2..5)
        }
        holder.containerView.counselItemTimeText.text = createdTime

        holder.containerView.likeCountText.text = list[position].commentLikes.toString()

        if(list[position].like) holder.containerView.likeImage.setImageResource(R.drawable.ic_like_pressed)
        else holder.containerView.likeImage.setImageResource(R.drawable.ic_like_unpressed)

        holder.containerView.likeImage.setOnClickListener {
            event.value = CounselListEvent.OnCounselItemClick(list[position].commentId)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}