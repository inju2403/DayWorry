## Link    
[Play Store](https://play.google.com/store/apps/details?id=com.inju.dayworryandroid)       
[하루고민 영상](https://www.youtube.com/watch?v=uprBOfF8Jzs)      

# 하루 고민 (Android Sharing Concerns App)    

<div><img src="https://user-images.githubusercontent.com/56947879/95682122-8e3f5700-0c1e-11eb-8146-10ba6a930e03.jpg"  width="100%"></div>
<div><img src="https://user-images.githubusercontent.com/56947879/95682124-90091a80-0c1e-11eb-9890-ac2fca21514a.jpg"  width="100%"></div>
<div><img src="https://user-images.githubusercontent.com/56947879/95682125-913a4780-0c1e-11eb-9ec5-2e6b531f4ba2.jpg"  width="100%"></div>
<div><img src="https://user-images.githubusercontent.com/56947879/95682127-926b7480-0c1e-11eb-94e1-2e1bbd6ad178.jpg"  width="100%"></div>
<div><img src="https://user-images.githubusercontent.com/56947879/95682128-939ca180-0c1e-11eb-9677-8236e31994d0.jpg"  width="100%"></div>
<div><img src="https://user-images.githubusercontent.com/56947879/95682131-97c8bf00-0c1e-11eb-977b-fb45c76de4b9.jpg"  width="100%"></div>
<div><img src="https://user-images.githubusercontent.com/56947879/95682134-9d260980-0c1e-11eb-9cb5-5e880b500723.jpg"  width="100%"></div>

    
## Version     
1.0.5      
    

## Project Description    

'하루 고민'은?

오늘하루 고민하고, 내일은 잊자!    
하고는 마음 한편 쌓아두었던 고민을 24시간 동안 풀어놓고 더 나은 내일을 보낼 수 있도록 도와주는 고민상담 커뮤니티 입니다.

## Background    

하나의 고민을 계속해서 안고 있지 말고 커뮤니티에 공유하고 이를 딱 하루만 다 같이 고민한 후에 날려버림으로써 개인의 고민을 해소하고 나아가 사회적으로 긍정적인 효과를 기대할 수 있음


## Role    

안드로이드 모바일 어플리케이션 UI/UX, 아키텍쳐 설계 및 구축, 데이터 시각화 및 전처리, User contacts    


## Used Libraries
 - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) (Store and manage UI-related data)
 - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)  (Observable data)
 - [Retrofit](https://github.com/square/retrofit) (HTTP client)
 - [Gson](https://github.com/google/gson) (A Java library that can be used to convert Java Objects into their JSON representation)
 - [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) (Light-weight threads)         
 - [Glide](https://github.com/bumptech/glide) (Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.)    
 - [Android Sliding Up Panel](https://github.com/umano/AndroidSlidingUpPanel) (This library provides a simple way to add a draggable sliding up panel (popularized by Google Music and Google Maps) to your Android application.)     
 - [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2?hl=ko) (Swipe views allow you to navigate between sibling screens, such as tabs, with a horizontal finger gesture, or swipe)    
 - [dotsindicator](https://github.com/tommybuonomo/dotsindicator) (This library makes it possible to represent View Pager Dots Indicator with 3 different awesome styles. It supports ViewPager and ViewPager2)      
 - [BottomSheet](https://github.com/Flipboard/bottomsheet) (BottomSheet is an Android component which presents a dismissible view from the bottom of the screen. BottomSheet can be a useful replacement for dialogs and menus but can hold any view so the use cases are endless.)     
 - [Lottie](https://github.com/airbnb/lottie-android) (Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile)    

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
    * **Blog**    - (https://blog.naver.com/inju2403)



 
## Licence
```
Copyright 2020 LEE SEUNGJU.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
