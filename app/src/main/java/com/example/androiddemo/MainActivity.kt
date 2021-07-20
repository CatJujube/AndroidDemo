package com.example.androiddemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.demo.parcelable.ParcelableDemoActivity


class MainActivity : AppCompatActivity() {
    companion object{
        val TAG = "MainActivity"
    }

    private var mDataList:MutableList<HomePageItem>? = mutableListOf()
    private val mContext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        if(!mDataList.isNullOrEmpty()) {
            val recyclerView = findViewById<RecyclerView>(R.id.homepage_rv)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            val adapter = mDataList?.let { HomePageAdapter(it) }
            recyclerView.adapter = adapter
        }else{
            Toast.makeText(this, "data list is empty!", Toast.LENGTH_SHORT).show()
            Log.i(TAG,"data list is empty!")
        }
    }

    private fun initData(){
        mDataList?.add(HomePageItem("Parcelable Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<Parcelable Demo>> is clicked!")
                Intent(mContext, ParcelableDemoActivity::class.java).apply { startActivity(this) }
            }
        }))
    }
}