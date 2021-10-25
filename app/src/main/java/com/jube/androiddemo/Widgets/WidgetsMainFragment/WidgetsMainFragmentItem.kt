package com.jube.androiddemo.Widgets.WidgetsMainFragment

class WidgetsMainFragmentItem(title:String, callback: IClickCallback) {
    var mTitle:String=title
    var mCallback:IClickCallback=callback
    interface IClickCallback{
        fun click()
    }
}