package com.inju.dayworry.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.EditBottomSheetFragment
import com.inju.dayworry.utils.ReportBottomSheetFragment
import com.inju.dayworry.worry.worryList.WorryItemViewHolder
import com.inju.dayworry.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_worry.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@RequiresApi(26)
class MyWorryListAdapter(private val list: MutableList<Worry>,
                         activity: MainActivity,
                         val event: MutableLiveData<WorryListEvent> = MutableLiveData()
) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    private val timeFormat = SimpleDateFormat("HH : mm")

    private val activity = activity
    val pref = activity.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val supportFragmentManager = activity.supportFragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_worry_current, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        holder.containerView.title.text = list[position].title
        holder.containerView.content.text = list[position].content
        holder.containerView.setOnClickListener {
            event.value = WorryListEvent.OnMyWorryItemClick(list[position].postId!!)
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
        holder.containerView.worryItemTimeText.text = remainTime


        holder.containerView.moreImage.setOnClickListener {
            //내 글 수정
            val editBottomSheetFragment = EditBottomSheetFragment(list[position].postId, 2)

            editBottomSheetFragment.show(supportFragmentManager, "editBottomSheetFragment")
        }
        holder.containerView.commentCountText.text = list[position].commentNum.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}