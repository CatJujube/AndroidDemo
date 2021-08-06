package com.example.androiddemo.Demo.parcelable

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.androiddemo.R

class ParcelableDemoPage1 : Activity() {
    private var mData:MsgJsonBean? = null
    companion object{
        const val TAG = "ParcelableDemoPage1"
        const val REQUEST_CODE_OK = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        this.setContentView(R.layout.parcelable_demo_page)

        val button:Button = this.findViewById(R.id.parcelable_demo1_btn)
        button.setOnClickListener {
            startActivityForResult(Intent(this,ParcelabelDemoPage2::class.java).apply {
                this.putExtra("msg_json_bean", Bundle().apply  {
                    this.putParcelable("data", mData)
                })
            }, REQUEST_CODE_OK)
        }
    }

    private fun initData(){
        val imageMessage = ImageMessage().apply {
            this.imgUrl="https://www.kotlincn.net/docs/reference/classes.html"
            this.localPath="xxx"
            this.mediaId="xxx"
        }
        val linkMessage = LinkMessage().apply {
            this.desc="xxx"
            this.imgUrl="xxx"
            this.title="xxx"
            this.url="xxx"
        }
        val videoMessage = VideoMessage().apply {
            this.mediaId="xxx"
        }
        mData = MsgJsonBean().apply {
            this.type = MsgJsonBean.MsgType.IMAGE
            this.data = imageMessage
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_OK && resultCode == Activity.RESULT_OK){
            Log.i(TAG,"activity return success!")
        }else{
            Log.i(TAG,"activity return cancel")
        }
    }
}