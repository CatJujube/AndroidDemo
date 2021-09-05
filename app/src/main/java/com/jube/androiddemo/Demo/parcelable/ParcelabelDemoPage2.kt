package com.jube.androiddemo.Demo.parcelable

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.jube.androiddemo.R

class ParcelabelDemoPage2 :Activity(){

    companion object{
        const val TAG = "ParcelabelDemoPage2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.parcelable_demo_page_2)
        val bundle = this.intent.getBundleExtra("msg_json_bean")
        val bundleData:MsgJsonBean? = bundle?.getParcelable("data")
        when(bundleData?.type){
            MsgJsonBean.MsgType.TEXT -> {
                Log.i(TAG, bundleData.data as String)
            }

            MsgJsonBean.MsgType.IMAGE -> {
                (bundleData.data as ImageMessage).imgUrl?.let { Log.i(TAG, it) }

            }

            MsgJsonBean.MsgType.LINK -> {
                (bundleData.data as LinkMessage).imgUrl.let { Log.i(TAG, it) }
            }

            MsgJsonBean.MsgType.VIDEO -> {
                (bundleData.data as VideoMessage).mediaId.let { Log.i(TAG, it) }
            }

            else -> {}
        }

        val button:Button = findViewById(R.id.parcelable_demo2_btn)
        button.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        setResult(Activity.RESULT_OK)
//    }
}