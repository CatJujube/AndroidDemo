package com.jube.androiddemo.Widgets.ListFragment

open class ListFragmentItem(title:String, callback:IListFragmentCallback) {
    val mTitle:String = title
    val mCallback:IListFragmentCallback = callback
    interface IListFragmentCallback{
        fun onClick()
    }
}