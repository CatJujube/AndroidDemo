package com.jube.androiddemo

class HomePageItem(title: String, callback: HomePageItemCallback) {
    var mTitle:String? = null
    var mCallback:HomePageItemCallback? = null

    init {
        mTitle = title
        mCallback = callback
    }

    interface HomePageItemCallback{
        fun onClick()
    }
}