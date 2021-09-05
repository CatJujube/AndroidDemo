package com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.jube.androiddemo.R

class CameraXSavePicLocalEditPhotoActivity : Activity() ,View.OnClickListener{
    private var mPictureUri:String? = null
    private lateinit var mBottomMiddleButton: Button
    private lateinit var mEditButton: Button
    private lateinit var mRefreshButton: Button
    private lateinit var mImagePreview:ImageView

    companion object{
        const val TAG="CameraXSavePicLocalEditPhotoActivity_LogMsg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_xsave_pic_local_edit_photo)
        initData()
        initUI()
        initEvent()
    }

    private fun initData(){
        mPictureUri = this@CameraXSavePicLocalEditPhotoActivity.intent.getStringExtra("save_uri")
        Log.i(TAG,"picture uri is: $mPictureUri")

    }

    private fun initUI(){
        mBottomMiddleButton = findViewById(R.id.camera_edit_middle_button)
        mEditButton = findViewById(R.id.camera_edit_left_button)
        mRefreshButton = findViewById(R.id.camera_edit_right_button)
        mImagePreview = findViewById(R.id.edit_image_preview)
        if(!mPictureUri.isNullOrEmpty()) {
            mImagePreview.setImageURI(Uri.parse(mPictureUri))
        }else{
            Log.e(TAG,"picture uri is empty")
        }
    }

    private fun initEvent(){
        mBottomMiddleButton.setOnClickListener(this)
        mEditButton.setOnClickListener(this)
        mRefreshButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.camera_edit_left_button -> {   //刷新
                this.setResult(Activity.RESULT_OK)
                finish()
            }

            R.id.camera_edit_middle_button -> {      //ok
                this.setResult(Activity.RESULT_OK)
                finish()
            }

            R.id.camera_edit_right_button->{    //edit
                this.setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }


    override fun onStop() {
        super.onStop()
        this.setResult(Activity.RESULT_OK)
    }
}