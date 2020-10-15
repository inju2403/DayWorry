package com.inju.dayworry.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.worry.worryList.WorryItemViewHolder
import com.inju.dayworry.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_story.view.*
import kotlinx.android.synthetic.main.item_story.view.profileImage
import kotlinx.android.synthetic.main.item_worry.view.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@RequiresApi(26)
class StoryAdapter(private val list: MutableList<Worry>,
                   activity: MainActivity,
                   val event: MutableLiveData<WorryListEvent> = MutableLiveData()
) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    private val activity = activity
    val pref = activity.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val supportFragmentManager = activity.supportFragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        holder.containerView.setOnClickListener {
            event.value = WorryListEvent.OnStoryItemClick(list[position].postId)
        }

        //        val currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val currentTime = LocalDateTime.now()
        val worryTime = LocalDateTime.parse(list[position].createdDate)

        val day: Long = (60 * 60 * 24).toLong()
        val remainTimeSec = day - ChronoUnit.SECONDS.between(worryTime, currentTime)
        val remainHour = remainTimeSec / 3600
        var remainMinute = ((remainTimeSec/60) - remainHour*60).toString()
        if(remainMinute.length == 1) remainMinute = "0$remainMinute"
        val remainTime = "$remainHour:$remainMinute"
        holder.containerView.timeText.text = remainTime

        val imageUrl = list[position].userProfileImage
        Glide.with(activity).load(imageUrl)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
                            .into(holder.containerView.profileImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}