package com.inju.dayworryandroid.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.inju.dayworryandroid.MainActivity
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.model.pojo.Worry
import com.inju.dayworryandroid.utils.Constants
import com.inju.dayworryandroid.utils.EditBottomSheetFragment
import com.inju.dayworryandroid.utils.ReportBottomSheetFragment
import com.inju.dayworryandroid.worry.worryList.WorryItemViewHolder
import com.inju.dayworryandroid.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_worry.view.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@RequiresApi(26)
class WorryListAdapter(private val list: MutableList<Worry>,
                       activity: MainActivity,
                       val event: MutableLiveData<WorryListEvent> = MutableLiveData()) :
    RecyclerView.Adapter<WorryItemViewHolder> ()
{

    private val activity = activity
    val pref = activity.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val supportFragmentManager = activity.supportFragmentManager

    private var userId = pref.getString("userId", "").toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worry, parent, false)
        return WorryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorryItemViewHolder, position: Int) {
        holder.containerView.title.text = list[position].title
        holder.containerView.content.text = list[position].content
        holder.containerView.setOnClickListener {
            event.value =
                WorryListEvent.OnWorryItemClick(
                    list[position].postId
                )
        }
        holder.containerView.moreImage.setOnClickListener {
            if(userId ==list[position].userId) {
                //내 글이면 수정
                val editBottomSheetFragment = EditBottomSheetFragment(list[position].postId, 0)

                editBottomSheetFragment.show(supportFragmentManager, "editBottomSheetFragment")
            }
            else {
                //내 글이 아니면 신고
                val reportBottomSheetFragment = ReportBottomSheetFragment(list[position].postId, 0)

                reportBottomSheetFragment.show(supportFragmentManager, "reportBottomSheetFragment")
            }
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

        holder.containerView.commentCountText.text = list[position].commentNum.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}