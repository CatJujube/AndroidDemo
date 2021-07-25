package com.example.androiddemo.demo.parcelable

import android.os.Parcel
import android.os.Parcelable

/*
wx.invoke("shareToExternalMoments", {
      text: {
            content:"",    // 文本内容
      },
      attachments: [
            {
                  msgtype: "image",    // 消息类型，必填
                  image: {
                        mediaid: "",      // 图片的素材id
                        imgUrl: "",        // 图片的imgUrl,跟图片mediaid填其中一个即可
                  },
            },
            {
                  msgtype: "link",    // 消息类型，必填
                  link: {
                        title: "",        // H5消息标题
                        imgUrl: "",    // H5消息封面图片URL
                        desc: "",    // H5消息摘要
                        url: "",        // H5消息页面url 必填
                  },
            },
            {
                  msgtype: "video",    // 消息类型，必填
                  video:{
                        mediaid:"",        // 视频的素材id
                 },
            },
      ]},function(res) {
        if (res.err_msg == "shareToExternalMoments:ok") {
        }
    }
);
* */

class MsgJsonBean() :Parcelable{
    var data:Any? = null
    var type = MsgType.NONE

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
        when{
            type==MsgType.TEXT -> data=parcel.readString()
        }
    }

    /**
     * 序列化功能
     * 序列化功能是由 writeToParcel 方法完成的，是通过 Parcel 的一些了 write 方法完成的。
     * 注意：序列化的类的成员属性也必须是可序列化的。Parcel 的 writeParcelable 方法中除了可序列化的属性，还需要添加 int 类型的 flag 参数，表示该对象应以何种方式写入，一般传 writeToParcel 方法的 flag 参数或者0。
     **/
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(type)
        when{
            type==MsgType.TEXT && data is String -> dest.writeString(data as String)
            type==MsgType.IMAGE && data is ImageMessage -> dest.writeParcelable(data as ImageMessage,0)
            type==MsgType.LINK && data is LinkMessage -> dest.writeParcelable(data as LinkMessage, 0)
            type==MsgType.VIDEO && data is VideoMessage -> dest.writeParcelable(data as VideoMessage, 0)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    /**反序列化功能
     *反序列化过程由 CREATOR 来完成，其内部表明了如何创建序列化对象和序列化对象的数组，创建序列化对象使用了序列化对象的参数为 Parcel 的构造方法，该方法中根据序列化过程得到的 Parcel 对象的 read 系列方法将序列化对象的内容还原完成反序列化过程。
     * 如果序列化类的属性有可序列化对象，在反序列化过程中需要传递当前线程的上下文类加载器，否则会报无法找到类的错误
     **/
    companion object CREATOR : Parcelable.Creator<MsgJsonBean> {
        override fun createFromParcel(parcel: Parcel): MsgJsonBean {
            return MsgJsonBean(parcel)
        }

        override fun newArray(size: Int): Array<MsgJsonBean?> {
            return arrayOfNulls(size)
        }
    }

    interface MsgType {
        companion object {
            const val NONE = 0
            const val TEXT = 1
            const val IMAGE = 2
            const val VIDEO = 3
            const val LINK = 4
        }
    }
}


