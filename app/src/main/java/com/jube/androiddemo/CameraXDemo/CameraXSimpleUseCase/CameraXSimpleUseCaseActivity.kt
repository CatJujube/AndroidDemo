package com.jube.androiddemo.CameraXDemo.CameraXSimpleUseCase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.jube.androiddemo.R

class CameraXSimpleUseCaseActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var mCameraSwitchButton: ImageButton
    private lateinit var mCameraCaptureButton: ImageButton
    private lateinit var mCameraPhotoViewButton: ImageButton
    private lateinit var mCameraFlashSwitchButton: ImageButton
    private var mCameraXPreView:CameraX? = null

    companion object {
        const val TAG = "CameraXSimpleUseCaseActivity_Log"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_xsimple_use_case)
        initUI()
        startPreview()
    }


    private fun initUI(){
        mCameraCaptureButton = findViewById(R.id.camera_capture_button)
        mCameraSwitchButton = findViewById(R.id.camera_flip_button)
        mCameraPhotoViewButton = findViewById(R.id.photo_view_button)
        mCameraFlashSwitchButton = findViewById(R.id.flash_switch_button)

        mCameraFlashSwitchButton.setOnClickListener(this)
        mCameraCaptureButton.setOnClickListener(this)
        mCameraPhotoViewButton.setOnClickListener(this)
        mCameraSwitchButton.setOnClickListener(this)

        initCameraXUI()
    }

    private fun initCameraXUI(){
        if(CameraX.usingPreviewView){
            Log.i(CameraX.TAG,"using PreviewView")
            val previewView = findViewById<PreviewView>(R.id.findView_camerax)
            mCameraXPreView = CameraX(this,previewView)
        }else {
            Log.i(CameraX.TAG,"using TextureView")
//            mCameraXPreView = CameraX(this)
//            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            params.gravity = Gravity.CENTER_VERTICAL
//            (findViewById<ViewGroup>(R.id.frame_layout)).addView(mCameraXPreView, 0, params)
            mCameraXPreView = findViewById(R.id.cmaerax_view)
        }
    }

    private fun startPreview(){
        mCameraXPreView?.startPreview()
    }

    private fun setPhotoViewButton(uri: Uri){
        mCameraPhotoViewButton.let {
                photoViewButton->
            photoViewButton.post {
                // Remove thumbnail padding
                photoViewButton.setPadding(4)
                // Load thumbnail into circular button using Glide
                Glide.with(photoViewButton)
                    .load(uri)
                    .apply(circleCropTransform())
                    .into(photoViewButton)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.camera_capture_button -> {
                mCameraXPreView?.takePicture(
                    object :CameraX.TakePhotoCallback{
                        override fun success(savedUri: Uri) {
                            setPhotoViewButton(uri = savedUri)
                        }

                        override fun fail() {
                            Log.e(TAG,"take picture error!")
                        }
                    }
                )
            }

            R.id.camera_flip_button -> {
                mCameraXPreView?.flip()
            }

            R.id.photo_view_button -> {

            }

            R.id.flash_switch_button -> {
                mCameraXPreView?.setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            }

            else -> {
                Log.e(TAG,"button type error")
            }
        }
    }
}