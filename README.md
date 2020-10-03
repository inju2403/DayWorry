# 하루 고민 (Android Sharing Concerns App)    

<div><img src="https://user-images.githubusercontent.com/56947879/94985972-cfd46000-0595-11eb-84b3-cfe2b0e5d6fd.png"  width="100%"></div>


## Project Description    

'하루 고민'은?

마음 한켠 쌓아두었던 고민을
하루동안 풀어놓고 마음을 비울 수 있도록 도와주는
고민 상담 서비스

## Background    

하나의 고민을 계속해서 안고 있지 말고 커뮤니티에 공유하고 이를 딱 하루만 다 같이 고민한 후에 날려버림으로써 사회적으로 긍정적인 효과를 기대할 수 있음


## Role    

안드로이드 모바일 어플리케이션 UI/UX, 아키텍쳐 설계 및 구축, 데이터 시각화 및 전처리, User contacts    


## Used Libraries
 - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) (Store and manage UI-related data)
 - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)  (Observable data)
 - [Retrofit](https://github.com/square/retrofit) (HTTP client)
 - [Gson](https://github.com/google/gson) (A Java library that can be used to convert Java Objects into their JSON representation)
 - [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) (Light-weight threads)
 - [RxJava](https://github.com/ReactiveX/RxJava) (RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.)
 - [DiffUtil](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil) (A utility class that calculates the difference between two lists and outputs a list of update operations that converts the first list into the second one)    
 - [Glide](https://github.com/bumptech/glide) (Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.)    
 - [Android Sliding Up Panel](https://github.com/umano/AndroidSlidingUpPanel) (This library provides a simple way to add a draggable sliding up panel (popularized by Google Music and Google Maps) to your Android application.)     
 - [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2?hl=ko) (Swipe views allow you to navigate between sibling screens, such as tabs, with a horizontal finger gesture, or swipe)    
 - [dotsindicator](https://github.com/tommybuonomo/dotsindicator) (This library makes it possible to represent View Pager Dots Indicator with 3 different awesome styles. It supports ViewPager and ViewPager2)     



## Architecture

<div><img src="https://user-images.githubusercontent.com/56947879/94985982-dbc02200-0595-11eb-8d85-863ad6ed99e4.png" align="left" width="100%"></div>

This Sharing Concerns App uses MVVM architecture. There is also a Repository layer, which is for interacting with API calls or DB transactions.

```kt

// View
viewModel!!.worryList.observe (this, Observer {
    // TODO
})

// ViewModel
private val worryListState = MutableLiveData<List<Worry>>()
val worryList: LiveData<List<Worry>> get() = worryListState

worryListState.value = repo.getWorrys() // get data from API and/or DB
```

## Author

* **LEE SEUNGJU**
    * **Github** - (https://github.com/inju2403)
    * **Blog**    - (https://inju2403.blog.me)
