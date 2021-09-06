package com.jube.androiddemo.OpenGLESDemos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.OpenGLESDemos.CameraXAndOpenglEs.CameraXAndOpenglEsActivity

import com.jube.androiddemo.HomePageAdapter
import com.jube.androiddemo.HomePageItem
import com.jube.androiddemo.MainActivity
import com.jube.androiddemo.R


class OpenglEsMainActivity : AppCompatActivity() {
    private var mDataList:MutableList<HomePageItem>? = mutableListOf()
    private val mContext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_es_main)
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
        mDataList?.add(HomePageItem("Draw Rectangle", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(MainActivity.TAG,"<<Draw Rectangle>> is clicked!")
                Intent(mContext, OpenglEsDrawRectActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("CameraXAndOpenglEs", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(MainActivity.TAG,"<<CameraXAndOpenglEs>> is clicked!")
                Intent(mContext, CameraXAndOpenglEsActivity::class.java).apply { startActivity(this) }
            }
        }))
    }
}

