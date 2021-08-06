package com.example.androiddemo.Demo.parcelable

import android.os.Parcel
import android.os.Parcelable

class LinkMessage() : Parcelable{
    var title:String = ""        // H5消息标题
    var imgUrl:String = ""    // H5消息封面图片URL
    var desc:String = ""    // H5消息摘要
    var url:String = ""        // H5消息页面url 必填

    constructor(parcel: Parcel) : this() {
        title = parcel.readString().toString()
        imgUrl = parcel.readString().toString()
        desc = parcel.readString().toString()
        url = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imgUrl)
        parcel.writeString(desc)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LinkMessage> {
        override fun createFromParcel(parcel: Parcel): LinkMessage {
            return LinkMessage(parcel)
        }

        override fun newArray(size: Int): Array<LinkMessage?> {
            return arrayOfNulls(size)
        }
    }
}