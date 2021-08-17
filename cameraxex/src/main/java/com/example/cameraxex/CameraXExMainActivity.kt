package com.example.cameraxex

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CameraXExMainActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var mCameraSwitchButton: ImageButton
    private lateinit var mCameraCaptureButton: ImageButton
    private lateinit var mCameraPhotoViewButton: ImageButton
    private lateinit var mCameraFlashSwitchButton: ImageButton
    private lateinit var mCameraParame:CameraXEx_Params
    private lateinit var mCamera:CameraXExMoudle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_xex_main)
        initData()
        initUI()
        mCamera.initCamera()
    }

    private fun initData(){
        mCameraParame = CameraXEx_Params()
        mCameraParame.imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
        mCameraParame.cameraControl = null
        mCameraParame.cameraInfo = null
        mCameraParame.cameraPreview = findViewById(R.id.viewFinder)
        mCameraParame.focusView = findViewById(R.id.focus_view)
        mCamera = CameraXExMoudle(this,mCameraParame)
    }

    private fun initUI(){
        mCameraCaptureButton = findViewById(R.id.camera_capture_button)
        mCameraSwitchButton = findViewById(R.id.camera_switch_button)
        mCameraPhotoViewButton = findViewById(R.id.photo_view_button)
        mCameraFlashSwitchButton = findViewById(R.id.flash_switch_button)

        mCameraFlashSwitchButton.setOnClickListener(this)
        mCameraCaptureButton.setOnClickListener(this)
        mCameraPhotoViewButton.setOnClickListener(this)
        mCameraSwitchButton.setOnClickListener(this)
    }

    private fun setPhotoViewButton(uri: Uri){
        mCameraPhotoViewButton?.let {
            photoViewButton->
            photoViewButton.post {
                // Remove thumbnail padding
                photoViewButton.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())
                // Load thumbnail into circular button using Glide
                Glide.with(photoViewButton)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photoViewButton)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.camera_capture_button -> {
                mCamera.takePhoto(object :CameraXExMoudle.OnTackPhotoCallback{
                    override fun onSuccess(uri: Uri) {
                        setPhotoViewButton(uri)
                    }

                    override fun onFail() {
                        Log.e(CameraXExMoudle.TAG,"take picture error!")
                    }

                })
            }

            R.id.camera_switch_button -> {
                when(mCameraParame.lenFacing){
                    CameraSelector.LENS_FACING_BACK -> {
                        mCamera.setLenFacing(CameraSelector.LENS_FACING_FRONT)
                    }

                    CameraSelector.LENS_FACING_FRONT -> {
                        mCamera.setLenFacing(CameraSelector.LENS_FACING_BACK)
                    }

                    else -> {
                        Log.e(CameraXExMoudle.TAG,"len facing switch error!")
                    }
                }
            }

            R.id.photo_view_button -> {

            }

            R.id.flash_switch_button -> {
                when(mCameraParame.flashMode){
                    ImageCapture.FLASH_MODE_ON -> {
                        mCamera.setFlashMode(ImageCapture.FLASH_MODE_OFF)
                    }

                    ImageCapture.FLASH_MODE_OFF ->{
                        mCamera.setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                    }

                    ImageCapture.FLASH_MODE_AUTO -> {
                        mCamera.setFlashMode(ImageCapture.FLASH_MODE_ON)
                    }

                    else->{
                        Log.e(CameraXExMoudle.TAG,"flash switch error!")
                    }
                }
            }

            else -> {
                Log.e(CameraXExMoudle.TAG,"button type error")
            }
        }
    }
}