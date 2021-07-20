package com.example.androiddemo.demo.parcelable

import android.os.Parcel
import android.os.Parcelable

class VideoMessage() : Parcelable{
    var mediaId:String = ""

    constructor(parcel: Parcel) : this() {
        mediaId = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mediaId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoMessage> {
        override fun createFromParcel(parcel: Parcel): VideoMessage {
            return VideoMessage(parcel)
        }

        override fun newArray(size: Int): Array<VideoMessage?> {
            return arrayOfNulls(size)
        }
    }
}