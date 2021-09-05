package com.jube.NdkDemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.HomePageAdapter
import com.jube.androiddemo.HomePageItem
import com.jube.androiddemo.MainActivity
import com.jube.androiddemo.OpenGLESDemos.OpenglEsDrawRectActivity
import com.jube.androiddemo.R

class NDKMainActivity : AppCompatActivity() {
    private var mDataList:MutableList<HomePageItem>? = mutableListOf()
    private val mContext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ndkmain)
        initData()
        if(!mDataList.isNullOrEmpty()) {
            val recyclerView = findViewById<RecyclerView>(R.id.opengles_rv)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            val adapter = mDataList?.let { HomePageAdapter(it) }
            recyclerView.adapter = adapter
        }else{
            Toast.makeText(this, "data list is empty!", Toast.LENGTH_SHORT).show()
            Log.i(MainActivity.TAG,"data list is empty!")
        }
    }

    private fun initData() {
        mDataList?.add(HomePageItem("JniHelloWorldActivity", object : HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(MainActivity.TAG,"<<JniHelloWorldActivity>> is clicked!")
                Intent(mContext, JniHelloWorldActivity::class.java).apply { startActivity(this) }
            }
        }))
    }
}