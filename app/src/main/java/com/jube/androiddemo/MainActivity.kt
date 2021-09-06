package com.jube.androiddemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.FlutterDemo.FlutterMainActivity
import com.jube.androiddemo.Criminallntent.CrimeActivity
import com.jube.androiddemo.Demo.parcelable.ParcelableDemoPage1
import com.jube.androiddemo.Demo.viewmodel.QuizActivity
import com.jube.androiddemo.NerdLauncher.NerdMainActivity
import com.jube.KotlinDemo.KotlinActivity
import com.jube.NdkDemo.NDKMainActivity
import com.jube.androiddemo.CameraXDemo.CameraXMainActivity
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.CameraXSavePicLocalMainActivity
import com.jube.androiddemo.Demo.MirrorDemoMainActivity
import com.jube.androiddemo.Demo.extendframelayout.ExtendFrameLayoutMainActivity
import com.jube.androiddemo.DragAndDraw.DragAndDrawActivity
import com.jube.androiddemo.OpenGLESDemos.OpenglEsMainActivity
import com.jube.androiddemo.PhotoGallery.PhotoGalleryActivity

//import io.flutter.embedding.android.FlutterActivity


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
        mDataList?.add(HomePageItem("CameraX Demos", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<CameraX>> is clicked!")
                Intent(mContext, CameraXMainActivity::class.java).apply { startActivity(this) }
            }
        }))

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

        mDataList?.add(HomePageItem("Drag And Draw Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<Drag And Draw Demo>> is clicked!")
                Intent(mContext, DragAndDrawActivity::class.java).apply { startActivity(this) }
            }
        }))

//        mDataList?.add(HomePageItem("Flutter Demo", object :HomePageItem.HomePageItemCallback{
//            override fun onClick() {
//                Log.i(TAG,"<<Flutter Demo>> is clicked!")
//                startActivity(FlutterActivity.createDefaultIntent(mContext))
//            }
//        }))

        mDataList?.add(HomePageItem("ExtendFrameLayout Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<ExtendFrameLayout Demo>> is clicked!")
                Intent(mContext, ExtendFrameLayoutMainActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("Mirror Demo", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<Mirror Demo>> is clicked!")
                Intent(mContext, MirrorDemoMainActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("OpenGLES Demos", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<OpenGLES Demos>> is clicked!")
                Intent(mContext, OpenglEsMainActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("NDK Demos", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(TAG,"<<NDK Demos>> is clicked!")
                Intent(mContext, NDKMainActivity::class.java).apply { startActivity(this) }
            }
        }))
    }
}