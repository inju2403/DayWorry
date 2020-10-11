package com.inju.dayworry.utils

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inju.dayworry.R
import kotlinx.android.synthetic.main.select_profilephoto_bottomsheet_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SelectProfilePhotoBottomSheetFragment(profile_photo: ImageView) : BottomSheetDialogFragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var select = 1
    private var profile_photo = profile_photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_profilephoto_bottomsheet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)

        val pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()

        job = Job()

        setUpClickListener(editor)
    }

    private fun setUpClickListener(editor: SharedPreferences.Editor) {
        defaultImage1.setOnClickListener {
            resetCheckColor()
            defaultCheckImage1.setImageResource(R.drawable.ic_checked)
            select = 1
            editor.putString("profileImage", "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default_01.jpg")
            editor.commit()
        }
        defaultImage2.setOnClickListener {
            resetCheckColor()
            defaultCheckImage2.setImageResource(R.drawable.ic_checked)
            select = 2
            editor.putString("profileImage", "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default_04.jpg")
            editor.commit()
        }
        defaultImage3.setOnClickListener {
            resetCheckColor()
            defaultCheckImage3.setImageResource(R.drawable.ic_checked)
            select = 3
            editor.putString("profileImage", "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default_03.jpg")
            editor.commit()
        }
        defaultImage4.setOnClickListener {
            resetCheckColor()
            defaultCheckImage4.setImageResource(R.drawable.ic_checked)
            select = 4
            editor.putString("profileImage", "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default_02.jpg")
            editor.commit()
        }

        canselText.setOnClickListener {
            editor.putString("profileImageSet", "no")
            editor.commit()
            dismiss()
        }

        completeText.setOnClickListener {
            editor.putString("profileImageSet", "ok")
            editor.commit()
            when(select) {
                1 -> profile_photo.setImageResource(R.drawable.ic_profile_default1)
                2 -> profile_photo.setImageResource(R.drawable.ic_profile_default2)
                3 -> profile_photo.setImageResource(R.drawable.ic_profile_default3)
                4 -> profile_photo.setImageResource(R.drawable.ic_profile_default4)
            }
            dismiss()
        }
    }

    private fun resetCheckColor() {
        defaultCheckImage1.setImageResource(R.drawable.ic_unchecked)
        defaultCheckImage2.setImageResource(R.drawable.ic_unchecked)
        defaultCheckImage3.setImageResource(R.drawable.ic_unchecked)
        defaultCheckImage4.setImageResource(R.drawable.ic_unchecked)
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity!!, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }
}