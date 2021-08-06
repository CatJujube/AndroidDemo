package com.example.androiddemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.Criminallntent.CrimeActivity
import com.example.androiddemo.Demo.parcelable.ParcelableDemoPage1
import com.example.androiddemo.Demo.viewmodel.QuizActivity
import com.example.androiddemo.NerdLauncher.NerdMainActivity
import com.example.KotlinDemo.KotlinActivity
import com.example.androiddemo.PhotoGallery.PhotoGalleryActivity
import com.example.androiddemo.PhotoGallery.PhotoGalleryFragment


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
                Intent(mContext, ParcelableDemoPage1::class.java).apply { startActivity(this) }
            }
        }))
//        mDataList?.add(HomePageItem("MVVMDemoActivity Demo", object :HomePageItem.HomePageItemCallback{
//            override fun onClick() {
//                Log.i(TAG,"<<MVVMDemoActivity Demo>> is clicked!")
//                Intent(mContext, MVVMDemoActivity::class.java).apply { startActivity(this) }
//            }
//        }))

        mDataList?.add(HomePageItem("ViewModel Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<ViewModel Demo>> is clicked!")
                Intent(mContext, QuizActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("Criminallntent App", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<Criminallntent App>> is clicked!")
                Intent(mContext, CrimeActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("Kotlin Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<Kotlin Demo>> is clicked!")
                Intent(mContext, KotlinActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("NerdLauncher Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<NerdLauncher Demo>> is clicked!")
                Intent(mContext, NerdMainActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("Photo Gallery Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<Photo Gallery Demo>> is clicked!")
                Intent(mContext, PhotoGalleryActivity::class.java).apply { startActivity(this) }
            }
        }))
    }
}