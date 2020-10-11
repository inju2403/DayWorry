package com.inju.dayworry.mypage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inju.dayworry.MainActivity
import com.inju.dayworry.R
import com.inju.dayworry.login.LoginActivity
import com.inju.dayworry.login.SetProfileActivity
import com.inju.dayworry.retrofit.ApiService
import com.inju.dayworry.retrofit.RetrofitClient
import com.inju.dayworry.worry.worryList.adapter.MyWorryListAdapter
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.utils.Constants.TAG
import com.inju.dayworry.utils.MyDialog
import com.inju.dayworry.worry.worryDetail.WorryDetailActivity
import com.inju.dayworry.worry.worryList.WorryListViewModel
import com.inju.dayworry.worry.worryList.buildlogic.WorryListInjector
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MyPageFragment : Fragment(), CoroutineScope {

    private val disposables = CompositeDisposable()
    private val httpCall: ApiService? = RetrofitClient.getClient(Constants.API_BASE_URL)!!.create(
        ApiService::class.java)

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var worryListViewModel: WorryListViewModel?= null
    private lateinit var listAdapter: MyWorryListAdapter

    private var userId: Long = 1
    private lateinit var userName: String
    private lateinit var social: String
    private lateinit var profile_image: String
    private var defaultImage: String = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default_01.jpg"
    private val defaultLong: Long = 1

    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        pref = activity!!.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        userId = pref.getLong("userId", defaultLong)
        userName = pref.getString("userName", "").toString()
        social = pref.getString("social", "").toString()
        profile_image = pref.getString("profileImage", defaultImage).toString()
        if(social=="naver") socialLogo.setImageResource(R.drawable.ic_naver_login_mypage)
        usernameText.text = userName

        var imageUrl = profile_image
        Glide.with(this).load(imageUrl).into(profileImage)

        job = Job()

        setViewModel()
        setLayoutManager()
        observeViewModel()
        recycleviewBottomDetection()

        setUpClickListener()

        emptyPostLayout.visibility = View.GONE
        myPageLoadingUi.visibility = View.GONE
        newImage.visibility = View.GONE
        delteUserLogoutLoadingUi.visibility = View.GONE
    }

    private fun setViewModel() {
        worryListViewModel = activity!!.application!!.let {
            ViewModelProvider(activity!!.viewModelStore, WorryListInjector(
                requireActivity().application
            ).provideWorryListViewModelFactory())
                .get(WorryListViewModel::class.java)
        }
    }

    private fun setLayoutManager() {
        myCurrentWorryList.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
    }

    private fun observeViewModel() {
        worryListViewModel!!.let {
            it.editMyWorry.observe(
                viewLifecycleOwner,
                Observer {
                    val intent = Intent(activity, WorryDetailActivity::class.java).apply {
                        putExtra("WORRY_ID", it)
                    }
                    startActivity(intent)
                }
            )
            it.myWorryList.observe(viewLifecycleOwner,
                Observer {
                    myCurrentWorryList.layoutManager =
                        LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

                    listAdapter =
                        MyWorryListAdapter(it, (activity as MainActivity))

                    listAdapter.event.observe(
                        viewLifecycleOwner,
                        Observer {
                            worryListViewModel!!.handleEvent(it)
                        }
                    )

                    myCurrentWorryList.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun paging() = launch {
        myPageLoadingUi.visibility = View.VISIBLE

        worryListViewModel!!.getMyWorrys(userId).join()
        listAdapter.notifyDataSetChanged()

        myPageLoadingUi.visibility = View.GONE
    }

    private fun recycleviewBottomDetection() {
        myCurrentWorryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                var itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    //todo
                    paging()
                }

            }
        })
    }

    private fun setUpClickListener() {
        profileEditBtn.setOnClickListener {
            //계정 정보 수정
            startActivity(Intent(activity!!, EditUserActivity::class.java))
        }
        storageLayout.setOnClickListener {
            //내 글 보관함
            startActivity(Intent(activity!!, MyWorryHistoryActivity::class.java))
        }
        onOffText.setOnClickListener {
            //푸쉬 알림 켜기/끄기
            if(onOffText.text == "켜기") onOffText.text = "끄기"
            else onOffText.text = "켜기"
        }
        logoutLayout.setOnClickListener {
            val editor = pref.edit()
            //로그아웃
            val dialog = MyDialog(activity!!)
            dialog.start("로그아웃", "정말 로그아웃 하시겠어요?")
            dialog.setOnOKClickedListener {

                delteUserLogoutLoadingUi.visibility = View.VISIBLE
                if (social == "kakao") {
                    //카카오
                    UserApiClient.rx.logout()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제 됨")
                            delteUserLogoutLoadingUi.visibility = View.GONE
                            showToast("로그아웃 되었습니다.")
                            val editor = pref.edit()
                            editor.clear()
                            editor.putBoolean("runFirst", false)
                            editor.commit()

                            startActivity(
                                Intent(
                                    activity,
                                    LoginActivity::class.java
                                )
                            )
                            activity!!.finish()
                        }, { error ->
                            Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제 됨", error)
                            delteUserLogoutLoadingUi.visibility = View.GONE
                            showToast("다시 시도해주세요.")
                        }).addTo(disposables)
                } else if (social == "naver") {
                    //네이버
                    editor.clear()
                    editor.commit()
                    showToast("로그아웃 되었습니다.")
                    startActivity(
                        Intent(
                            activity,
                            LoginActivity::class.java
                        )
                    )
                    activity!!.finish()
                }
            }
        }
        deleteUserLayout.setOnClickListener {
            //계정삭제
            val dialog = MyDialog(activity!!)
            dialog.start("계정 삭제", "      하고 계정을 삭제하시겠어요?\n\n개인 정보와 내역이 모두 삭제됩니다.")
            dialog.setOnOKClickedListener {

                delteUserLogoutLoadingUi.visibility = View.VISIBLE
                httpCall?.deleteUser(userId)?.enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d(TAG, "delete user failed onFailure")
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        when (response.code()) {
                            200 -> {
                                delteUserLogoutLoadingUi.visibility = View.GONE
                                showToast("계정이 삭제 되었습니다. 이용해주셔서 감사합니다.")
                                val editor = pref.edit()
                                editor.clear()
                                editor.putBoolean("runFirst", false)
                                editor.commit()

                                startActivity(
                                    Intent(
                                        activity,
                                        LoginActivity::class.java
                                    )
                                )
                                activity!!.finish()
                            }
                            else -> {
                                Log.d(TAG, "계정삭제 에러")
                                delteUserLogoutLoadingUi.visibility = View.GONE
                                showToast("다시 시도해주세요.")
                            }
                        }
                    }

                })
            }

        }
    }

    private fun initMyWorrys() = launch {
        worryListViewModel!!.initMyWorrys(userId).join()
        listAdapter.notifyDataSetChanged()

        if(worryListViewModel!!.getmyWorryListitemCnt() == 0) {
            emptyPostLayout.visibility = View.VISIBLE
            myCurrentWorryList.visibility = View.GONE
            newImage.visibility = View.GONE
        }
        else {
            emptyPostLayout.visibility = View.GONE
            myCurrentWorryList.visibility = View.VISIBLE
            newImage.visibility = View.VISIBLE
        }
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(activity!!, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }


    override fun onResume() {
        super.onResume()
        initMyWorrys()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        myCurrentWorryList.adapter = null
    }
}