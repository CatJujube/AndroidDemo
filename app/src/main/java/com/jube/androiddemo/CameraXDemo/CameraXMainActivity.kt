package com.jube.androiddemo.CameraXDemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.CameraXSavePicLocalMainActivity
import com.jube.androiddemo.CameraXDemo.CameraXSimpleUseCase.CameraXSimpleUseCaseActivity
import com.jube.androiddemo.HomePageAdapter
import com.jube.androiddemo.HomePageItem
import com.jube.androiddemo.MainActivity
import com.jube.androiddemo.OpenGLESDemos.CameraXAndOpenglEs.CameraXAndOpenglEsActivity
import com.jube.androiddemo.OpenGLESDemos.OpenglEsDrawRectActivity
import com.jube.androiddemo.R
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXMainActivity : AppCompatActivity() {
    private var mDataList:MutableList<HomePageItem>? = mutableListOf()
    private val mContext = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_xmain)
        initData()
        if(!mDataList.isNullOrEmpty()) {
            val recyclerView = findViewById<RecyclerView>(R.id.camerax_rv)
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
        mDataList?.add(HomePageItem("CameraX Save Pic Local", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(MainActivity.TAG,"<<CameraXSavePicLocalMainActivity>> is clicked!")
                Intent(mContext, CameraXSavePicLocalMainActivity::class.java).apply { startActivity(this) }
            }
        }))

        mDataList?.add(HomePageItem("CameraX Simple UseCase", object :HomePageItem.HomePageItemCallback{
            override fun onClick() {
                Log.i(MainActivity.TAG,"<<CameraXSimpleUseCaseActivity>> is clicked!")
                Intent(mContext, CameraXSimpleUseCaseActivity::class.java).apply { startActivity(this) }
            }
        }))

    }
}