package com.inju.dayworry.worry.worryList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.model.pojo.Worry
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.worry.worryList.WorryItemViewHolder
import com.inju.dayworry.worry.worryList.WorryListEvent
import kotlinx.android.synthetic.main.item_story.view.*
import kotlinx.android.synthetic.main.item_story.view.profileImage

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
        holder.containerView.timeText.text = list[position].createdDate.substring(11..15)

        val imageUrl = list[position].userProfileImage
        Glide.with(activity).load(imageUrl).into(holder.containerView.profileImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}