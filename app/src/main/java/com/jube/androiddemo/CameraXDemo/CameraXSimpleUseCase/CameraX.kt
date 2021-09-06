package com.jube.androiddemo.CameraXDemo.CameraXSimpleUseCase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.webkit.MimeTypeMap
import androidx.annotation.NonNull
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.CameraXSavePicLocalMainActivity.Companion.FILENAME_FORMAT
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.CameraXSavePicLocalMainActivity.Companion.PHOTO_EXTENSION
import com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal.FocusImageView
import com.jube.androiddemo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class CameraX :TextureView{
    private val mContext = context
    private lateinit var mImageCapture: ImageCapture
    private var mCameraPreview: PreviewView? = null
    private var focusView: FocusImageView? = null
    private lateinit var mPreview: Preview
    private var mExecutor = Executors.newSingleThreadExecutor()
    private lateinit var mCameraSelector: CameraSelector
    private lateinit var mCamera: Camera
    private lateinit var mCameraControl: CameraControl
    private lateinit var mCameraInfo:CameraInfo
    private var mLensFacing:Int = CameraSelector.LENS_FACING_BACK
    private lateinit var mCameraUseCaseGroup:UseCaseGroup
    private var mFlashMode:Int = ImageCapture.FLASH_MODE_AUTO
    private lateinit var mCameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var mCameraProvider:ProcessCameraProvider
    private var mIsFirstStartPreview:Boolean = true
    private lateinit var mOutputOption:ImageCapture.OutputFileOptions
    private var mIsTakingPreview:Boolean = false


    companion object{
        const val TAG = "CameraX_Log"
        const val usingPreviewView = false
    }

    constructor(context: Context):super(context){

    }

    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){

    }

    constructor(context: Context,preview: PreviewView):super(context){
        this.mCameraPreview = preview
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreview(){
        Log.i(TAG,"bindPreview")
        mPreview = Preview.Builder()
//                    .setTargetRotation()
//                    .setTargetResolution()
            .build()

        /**旋转角度，一般Preview会根据Preview.Builder.setTargetRotation(int)来设置旋转角度，当未设置时会根据Display.getRotation()的值来设置旋转角度*/
        val rotationInfo: Int = mPreview.targetRotation

        /**相机选中的预览尺寸，CameraX会根据Camera2的API在一系列尺寸中选择最适合的一个，这里返回的是选中的尺寸*/
        val resolutionInfo: Size? = mPreview.attachedSurfaceResolution

        /**
         *为Preview设置一个Surface，Surface可以传入ImageReader，MediaCodec，SurfaceTexture，TextureView，其中Surface中如果传入的是SurfaceView那么旋转方向会自动适配；如果是ImageReadder，MediaCodec则应该用户负责管理旋转；
         *在SurfaceTextViewz中可以调用SurfaceTexture.getTransformMatrix(float[])来获取原本的方向
         *TextureView也是总处于原本的方向
         **/
        when(usingPreviewView){
            true -> {
                mPreview.setSurfaceProvider(mCameraPreview?.surfaceProvider)
                Log.i(TAG,"using PreviewView init")
            }
            false -> {
                Log.i(TAG,"using TextureView init")
                mPreview.setSurfaceProvider { request ->
                    val surface = Surface(surfaceTexture)
                    request.provideSurface(surface, mExecutor, {
//                        surface.release()
//                        surfaceTexture?.release()
                        Log.v(TAG, "--accept------")
                    })
                    Log.i(TAG,"set surface provider")
                }
            }
        }
    }

    private fun bindImageCapture(){
        mImageCapture = ImageCapture
            .Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) //模式
            .setFlashMode(mFlashMode)   //闪光灯模式
            //.setBufferFormat(0)   //格式
            //.setTargetAspectRatio(RATIO_4_3)  //长宽比
            //.setTargetRotation(ROTATION_0)    //旋转角度
            .build()
    }

    private fun bindCameraSelector(){
        mCameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(mLensFacing)
            .build()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindUseCaseGroup(){
            bindProvider()
            bindPreview()
            bindImageCapture()
            bindCameraSelector()
            bindImageAnalysis()
            mCameraUseCaseGroup = UseCaseGroup
            .Builder()
            .addUseCase(mPreview)
            .addUseCase(mImageCapture)
            .build()
    }

    private fun bindProvider(){
        mCameraProvider = mCameraProviderFuture.get()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCamera(){
        mCameraProvider.unbindAll()
        mCamera = mCameraProvider.bindToLifecycle(
        mContext as LifecycleOwner,
        mCameraSelector,
        mCameraUseCaseGroup)
    }

    private fun bindCameraControl(){
        mCameraControl = mCamera.cameraControl
    }

    private fun bindCameraInfo(){
        mCameraInfo = mCamera.cameraInfo
    }

    fun startPreview(){
        mCameraProviderFuture = ProcessCameraProvider.getInstance(mContext)
        mCameraProviderFuture.addListener(Runnable {
                bindUseCaseGroup()
                bindCamera()
                bindCameraControl()
                bindCameraInfo()
//                loadFocusModule()
            }, ContextCompat.getMainExecutor(mContext))
    }

    private fun bindImageAnalysis(){

    }

    fun takePicture(callback:TakePhotoCallback){
        mImageCapture.let {
            val pictureSaveFile = getPictureSaveFile(mContext)
            val outputOption = ImageCapture.OutputFileOptions.Builder(pictureSaveFile).build()
            mImageCapture.takePicture(
                outputOption,
                ContextCompat.getMainExecutor(mContext),
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri ?: Uri.fromFile(pictureSaveFile)
                        Log.i(TAG,"Photo capture successed: ${savedUri}")

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            (mContext as Activity).sendBroadcast(
                                Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri)
                            )
                        }

                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(
                            mContext,
                            arrayOf(savedUri.toFile().absolutePath),
                            arrayOf(mimeType)){
                                _,uri -> Log.d(TAG,"Image capture scanned into media store:$uri")
                        }
                        callback.success(savedUri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        callback.fail()
                        Log.e(TAG,"image save error")
                    }
                }
            )
            return
        }
    }
    private fun getPictureSaveFile(context: Context): File {
        return File(getPictureOutputDirectory(context), SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis()) + PHOTO_EXTENSION)
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

    fun flip(){
        mLensFacing = when(mLensFacing){
            CameraSelector.LENS_FACING_FRONT -> CameraSelector.LENS_FACING_BACK
            CameraSelector.LENS_FACING_BACK -> CameraSelector.LENS_FACING_FRONT
            else -> CameraSelector.LENS_FACING_FRONT
        }
        startPreview()
    }

    fun setFlashMode(flashMode: Int){
        mFlashMode = flashMode
        startPreview()
    }

    interface TakePhotoCallback{
        fun success(savedUri: Uri)
        fun fail()
    }
}