package com.inju.dayworry.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints
import com.inju.dayworry.R

class MyDialog(context : Context) {

    private val RETURN_OK = 101

    private val dialog = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var titleText : TextView
    private lateinit var contentText : TextView
    private lateinit var yesBtn : Button
    private lateinit var noBtn : Button
    private lateinit var listener : MyDialogOKClickedListener

    fun start(title : String, content: String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dialog.setContentView(R.layout.dialog_layout)     //다이얼로그에 사용할 xml 파일을 불러옴
        dialog.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //다이얼로그 테두리 외부 색상을 투명하게 함

        titleText = dialog.findViewById(R.id.titleText)
        titleText.text = title

        contentText = dialog.findViewById(R.id.contentText)
        contentText.text = content

        yesBtn = dialog.findViewById(R.id.yesBtn)
        yesBtn.setOnClickListener {

            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            listener.onOKClicked(RETURN_OK)

            dialog.dismiss()
        }

        noBtn = dialog.findViewById(R.id.noBtn)
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnOKClickedListener(listener: (Int) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(chkNum: Int) {
                listener(chkNum)
            }
        }
    }


    interface MyDialogOKClickedListener {
        fun onOKClicked(chkNum: Int)
    }

}