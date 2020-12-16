package com.inju.dayworryandroid.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.inju.dayworryandroid.R
import com.inju.dayworryandroid.model.pojo.ID_CHECK_RETURN_POJO
import com.inju.dayworryandroid.model.pojo.SOCIAL_LOGIN_RETURN_POJO
import com.inju.dayworryandroid.retrofit.ApiService
import com.inju.dayworryandroid.retrofit.RetrofitClient
import com.inju.dayworryandroid.utils.Constants
import kotlinx.android.synthetic.main.activity_add_worry.*
import kotlinx.android.synthetic.main.fragment_set_profile.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.nextBtn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {

    private val httpCall: ApiService? = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
        ApiService::class.java)

    private var pw2Len = 0

    var litePupleColor = "#9689FC"
    var superLiteGreyColor = "#cbcdd5"
    var darkNavyColor = "#2e3042"
    var liteNavyColor = "#535974"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpClickListener()
        setTextChangeListener()
    }

    private fun setUpClickListener() {
        nextBtn.setOnClickListener {
            when {
                idEditText.text.toString().trim().isEmpty() -> {
                    showToast("아이디를 입력해주세요")
                }
                pwEditText.text.toString().trim().isEmpty() -> {
                    showToast("비밀번호를 입력해주세요")
                }
                pw2EditText.text.toString().trim().isEmpty() -> {
                    showToast("비밀번호를 한번  입력해주세요")
                }
                pwEditText.text.toString().trim() != pw2EditText.text.toString().trim() -> {
                    showToast("비밀번호가 일치하지 않습니다")
                }
                else -> {
                    httpCall?.checkId((activity as SetProfileActivity).userId)?.enqueue(object :
                        Callback<ID_CHECK_RETURN_POJO> {
                        override fun onFailure(call: Call<ID_CHECK_RETURN_POJO>, t: Throwable) {
                            Log.d(Constants.TAG, "kakaologin - onFailed() called / t: ${t}")
                            showToast("잠시 후에 다시 시도해주세요.")
                        }

                        override fun onResponse(call: Call<ID_CHECK_RETURN_POJO>, response: Response<ID_CHECK_RETURN_POJO>) {
                            when (response.code()) {
                                200 -> {
                                    if(response.body()?.flag == true) {
                                        //아이디 중복 x
                                        moveSetProfileFragment()
                                        (activity as SetProfileActivity).fragmentState =
                                            (activity as SetProfileActivity).FRAG_PROFILE
                                    }
                                    else {
                                        //아이디가 중복 됨
                                        showToast("이미 존재하는 아이디입니다.")
                                    }
                                }
                                400 -> {
                                    showToast("잠시 후에 다시 시도해주세요.")
                                }
                            }
                        }

                    })
                }
            }
        }

        loginText.setOnClickListener {
            startActivity(Intent(activity, TryLoginActivity::class.java))
            activity!!.finish()
        }
    }

    private fun setTextChangeListener() {
        idEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                (activity as SetProfileActivity).userId = s.toString().trim()

                if((activity as SetProfileActivity).userId.isNotEmpty() && (activity as SetProfileActivity).userPw.isNotEmpty() && pw2Len > 0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                else {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        pwEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                (activity as SetProfileActivity).userPw = s.toString().trim()

                if((activity as SetProfileActivity).userId.isNotEmpty() && (activity as SetProfileActivity).userPw.isNotEmpty() && pw2Len > 0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                else {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        pw2EditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                pw2Len = s.toString().trim().length

                if((activity as SetProfileActivity).userId.isNotEmpty() && (activity as SetProfileActivity).userPw.isNotEmpty() && pw2Len > 0) {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_background)
                    nextBtn.setTextColor(Color.parseColor(darkNavyColor))
                }
                else {
                    nextBtn.setBackgroundResource(R.drawable.next_btn_unselect_background)
                    nextBtn.setTextColor(Color.parseColor(superLiteGreyColor))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }

    private fun moveSetProfileFragment() {
        (activity as SetProfileActivity).switchSetProfileFragment()
    }
}