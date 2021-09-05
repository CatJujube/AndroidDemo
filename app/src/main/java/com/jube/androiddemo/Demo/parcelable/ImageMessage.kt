package com.jube.androiddemo.Demo.parcelable

import android.os.Parcel
import android.os.Parcelable

class ImageMessage() : Parcelable{
    var localPath:String? = null
    var mediaId:String? = null
    var imgUrl:String? = null

    constructor(parcel: Parcel) : this() {
        localPath = parcel.readString()
        mediaId = parcel.readString()
        imgUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(localPath)
        parcel.writeString(mediaId)
        parcel.writeString(imgUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageMessage> {
        override fun createFromParcel(parcel: Parcel): ImageMessage {
            return ImageMessage(parcel)
        }

        override fun newArray(size: Int): Array<ImageMessage?> {
            return arrayOfNulls(size)
        }
    }
}