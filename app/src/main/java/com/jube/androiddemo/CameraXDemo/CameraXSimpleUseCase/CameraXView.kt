package com.jube.androiddemo.CameraXDemo.CameraXSimpleUseCase

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Output
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.Surface.ROTATION_0
import android.view.TextureView
import androidx.camera.core.*
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.CameraXSavePicLocalMainActivity
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.FocusImageView
import com.jube.androiddemo.R
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class CameraXView : TextureView {
    private val mContext = context
    private var mImageCapture: ImageCapture? = null
    private var mCameraPreview: PreviewView? = null
    private var focusView: FocusImageView? = null
    private var mPreview:Preview? = null
    private var mExecutor = Executors.newSingleThreadExecutor()
    private var mCameraSelector:CameraSelector? = null
    private var mCamera:Camera? = null
    private var mCameraContorl:CameraControl? = null
    private var mCameraInfo:CameraInfo? = null
    private var mLensFacing:Int = 0
    private var mCameraUseCaseGroup:UseCaseGroup? = null
    private var mFlashMode:Int = ImageCapture.FLASH_MODE_AUTO
    private var mCameraProviderFuture:ListenableFuture<ProcessCameraProvider>? = null
    private var mCameraProvider:ProcessCameraProvider? = null
    private var mIsFirstStartPreview:Boolean = true
    private var mOutputOption:ImageCapture.OutputFileOptions? = null
    private var mIsTakingPreview:Boolean = false

    companion object{
        const val TAG = "CameraXView_Log"
    }

    constructor(context:Context):super(context){
    }

    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet){
    }

    constructor(context: Context, preview: PreviewView):this (context){
        this.mCameraPreview = preview
    }

    @SuppressLint("RestrictedApi")
    private fun bindImageCapture(){
        mImageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            .setBufferFormat(0)
//            .setTargetAspectRatio(RATIO_4_3)
            .setFlashMode(mFlashMode)
//            .setTargetRotation(ROTATION_0)
            .build()
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreview(){
        Log.i(TAG,"bindPreview")
        mPreview = Preview.Builder()
//                    .setTargetRotation()
//                    .setTargetResolution()
            .build()
        /**旋转角度，一般Preview会根据Preview.Builder.setTargetRotation(int)来设置旋转角度，当未设置时会根据Display.getRotation()的值来设置旋转角度*/
        val rotationInfo: Int? = mPreview?.targetRotation
        /**相机选中的预览尺寸，CameraX会根据Camera2的API在一系列尺寸中选择最适合的一个，这里返回的是选中的尺寸*/
        val resolutionInfo: Size? = mPreview?.attachedSurfaceResolution
        /**
         *为Preview设置一个Surface，Surface可以传入ImageReader，MediaCodec，SurfaceTexture，TextureView，其中Surface中如果传入的是SurfaceView那么旋转方向会自动适配；如果是ImageReadder，MediaCodec则应该用户负责管理旋转；
         *在SurfaceTextViewz中可以调用SurfaceTexture.getTransformMatrix(float[])来获取原本的方向
         *TextureView也是总处于原本的方向
         **/
        if(mCameraPreview==null) {
            Log.i(TAG,"using TextureView init")
            mPreview?.setSurfaceProvider {
                Preview.SurfaceProvider { request ->
                    val surface = Surface(this.surfaceTexture)
                    request.provideSurface(surface, mExecutor, Consumer {
                        surface.release()
                        Log.i(TAG, "consumer")
                    })
                }
            }
        }
    }

    private fun bindCameraSelector(){
        mCameraSelector = CameraSelector
                          .Builder()
                          .requireLensFacing(mLensFacing)
                          .build()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindUseCaseGroup(){
        Log.i(TAG,"bindUseCaseGroup")
        bindCameraSelector()
        bindImageCapture()
        bindPreview()
        bindOutputOption()
        mCameraUseCaseGroup = UseCaseGroup
            .Builder()
            .addUseCase(mPreview!!)
            .addUseCase(mImageCapture!!)
            .build()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindUserCase(){
        Log.i(TAG,"bindUserCase")
        if(mContext is LifecycleOwner && mCameraProvider!=null) {
                try {
                    bindUseCaseGroup()
                    mCameraProvider?.unbindAll()
                    mCamera = mCameraProvider?.bindToLifecycle(
                        mContext as LifecycleOwner,
                        mCameraSelector!!,
                        mCameraUseCaseGroup!!)
                    mCameraInfo = mCamera!!.cameraInfo
                    mCameraContorl = mCamera!!.cameraControl
                    if(mCameraPreview!=null){
                        mPreview?.setSurfaceProvider { mCameraPreview?.surfaceProvider }
                        Log.i(TAG,"using PreviewView init")
                    }
                    Log.i(TAG,"open camera success")
                } catch (e: Exception) {
                    Log.e(TAG, "open camera error, error=${e.message}")
                }
        }
    }

    private fun bindCameraProvider(){
        mCameraProviderFuture = ProcessCameraProvider.getInstance(mContext)
        mCameraProviderFuture?.addListener(Runnable {
                Log.i(TAG,"bindCameraProvider in sub thread")
                mCameraProvider = mCameraProviderFuture!!.get()
                bindUserCase()
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    fun startPreview(){
        Log.i(TAG,"startPreview")
        this.isOpaque = true
        mIsTakingPreview = true
        if(mIsFirstStartPreview){
            bindCameraProvider()
            mIsFirstStartPreview = false
        }else{
            bindUserCase()
        }
    }

    private fun getPictureSaveFile(): File {
        return File(getPictureOutputDirectory(context), SimpleDateFormat(
            CameraXSavePicLocalMainActivity.FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis()) + CameraXSavePicLocalMainActivity.PHOTO_EXTENSION
        )
    }

    private fun getPictureOutputDirectory(context: Context):File{
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it,appContext.resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir!=null && mediaDir.exists()) mediaDir else appContext.filesDir
    }

    private fun bindOutputOption(){
        val pictureSaveFile = getPictureSaveFile()
        mOutputOption = ImageCapture.OutputFileOptions.Builder(pictureSaveFile).build()
    }

    fun takePicture(callback: TakePhotoCallback){
        Log.i(TAG,"takePicture")
        mImageCapture?.takePicture(
            mOutputOption!!,
            ContextCompat.getMainExecutor(mContext),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        callback.success(it)
                    }
                    Log.e(TAG,"take picture succeed!")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG,"take picture error!, error=${exception.message}")
                    callback.fail()
                }
            }
        )
        if(mImageCapture == null) Log.i(TAG,"image capture is null")
    }

    fun flip(){
        mLensFacing = when(mLensFacing){
            CameraSelector.LENS_FACING_FRONT -> CameraSelector.LENS_FACING_BACK
            CameraSelector.LENS_FACING_BACK -> CameraSelector.LENS_FACING_FRONT
            else -> CameraSelector.LENS_FACING_FRONT
        }
        bindUseCaseGroup()
    }

    fun setFlashMode(flashMode: Int){
        mFlashMode = flashMode
        bindUseCaseGroup()
    }

    interface TakePhotoCallback{
        fun success(savedUri: Uri)
        fun fail()
    }
}

