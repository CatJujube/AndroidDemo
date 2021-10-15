package com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.hardware.Camera
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.SurfaceView
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jube.androiddemo.R
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CameraXSavePicLocalMainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var mCancelButton: Button
    private lateinit var mFlashButton: Button
    private lateinit var mBottomMiddleButton: Button
    private lateinit var mVideoButton:Button
    private lateinit var mTurnoverButton: Button
    private var focusView: FocusImageView? = null
    private var mImageCapture: ImageCapture? = null
    private var mCameraPreview: PreviewView? = null
    private var mCameraXParams: CameraXSavePicLocal_Params? = null
    private var mCameraInfo: CameraInfo? = null
    private var mCameraControl: CameraControl? = null

    private val mCameraXSavePicLocalModel:CameraXSavePicLocalViewModel by lazy {
        ViewModelProvider(this).get(CameraXSavePicLocalViewModel::class.java)
    }

    companion object{
        const val TAG = "CameraXSavePicLocalMainActivity_LogMsg"
        const val PHOTO_EXTENSION = ".jpg"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUESTCODE_EDIT_OK = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null){
            supportActionBar?.hide()
        }
        setContentView(R.layout.activity_camera_xsave_pic_local_main)
        initData()
        initUI()
        startCamera()
    }

    private fun initData(){
        mCameraXSavePicLocalModel.initData()
        if(mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value!=null) {
            mCameraXParams = mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value!!
        }else{
            mCameraXParams = CameraXSavePicLocal_Params()
        }
    }

    private fun updateCameraXParamesLiveData(){
        mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.postValue(mCameraXParams)
    }

    private fun initUI(){
        mCancelButton = findViewById(R.id.cancel_button)
        mFlashButton = findViewById(R.id.flash_button)
        mBottomMiddleButton = findViewById(R.id.camera_middle_button)
        mVideoButton = findViewById(R.id.camera_left_button)
        mTurnoverButton = findViewById(R.id.camera_right_button)
        mCameraPreview = findViewById(R.id.camera_preview)
        focusView = findViewById(R.id.focus_view)


        mCancelButton.setOnClickListener(this)
        mFlashButton.setOnClickListener(this)
        mBottomMiddleButton.setOnClickListener(this)
        mVideoButton.setOnClickListener(this)
        mTurnoverButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.cancel_button->{
                finish()
            }

            R.id.flash_button->{
                when(mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value?.flashMode){
                    ImageCapture.FLASH_MODE_ON -> {
                        setFlashMode(ImageCapture.FLASH_MODE_OFF)
                    }

                    ImageCapture.FLASH_MODE_OFF -> {
                        setFlashMode(ImageCapture.FLASH_MODE_ON)
                    }

                    else -> {
                        Log.e(TAG,"flash mode type error! type is ${mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value?.flashMode}")
                    }
                }
            }

            R.id.camera_middle_button->{
                takePicture()
            }

            R.id.camera_left_button->{

            }

            R.id.camera_right_button->{
                when(mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value?.lenFace){
                    CameraSelector.LENS_FACING_BACK -> {
                        setLenFacing(CameraSelector.LENS_FACING_FRONT)
                    }
                    CameraSelector.LENS_FACING_FRONT -> {
                        setLenFacing(CameraSelector.LENS_FACING_BACK)
                    }
                    else -> {
                        Log.e(TAG,"lens facing value error! value = ${mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value?.lenFace}")
                    }
                }
            }
            else->{
                Log.e(TAG,"button type error!")
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreview():Preview{
        val preview = Preview.Builder()
//                    .setTargetRotation()
//                    .setTargetResolution()
            .build()
        val rotationInfo:Int = preview.targetRotation                   /**旋转角度，一般Preview会根据Preview.Builder.setTargetRotation(int)来设置旋转角度，当未设置时会根据Display.getRotation()的值来设置旋转角度*/
        val resolutionInfo:Size? = preview.attachedSurfaceResolution    /**相机选中的预览尺寸，CameraX会根据Camera2的API在一系列尺寸中选择最适合的一个，这里返回的是选中的尺寸*/

        /**
         *为Preview设置一个Surface，Surface可以传入ImageReader，MediaCodec，SurfaceTexture，TextureView，其中Surface中如果传入的是SurfaceView那么旋转方向会自动适配；如果是ImageReadder，MediaCodec则应该用户负责管理旋转；
         *在SurfaceTextViewz中可以调用SurfaceTexture.getTransformMatrix(float[])来获取原本的方向
         *TextureView也是总处于原本的方向
         **/
        preview.setSurfaceProvider {
            Preview.SurfaceProvider { TODO("Not yet implemented") }
        }
        return preview
    }

    @SuppressLint("RestrictedApi")
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()

                val preview = bindPreview()
                mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.observe(this@CameraXSavePicLocalMainActivity,
                    Observer {  mCameraXSavePicLocalLiveData ->
                                mImageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
                                mImageCapture!!.flashMode = mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.value?.flashMode!!
                                val cameraSelector = mCameraXSavePicLocalLiveData?.let {
                                    CameraSelector.Builder().requireLensFacing(it.lenFace).build()
                                }
                                cameraProvider.unbindAll()
                                val camera = cameraSelector?.let {
                                    cameraProvider.bindToLifecycle(this@CameraXSavePicLocalMainActivity, it,preview,mImageCapture)
                                }
                            mCameraControl = camera?.cameraControl
                            mCameraInfo = camera?.cameraInfo
                            initCameraListener()
                    })

                preview.setSurfaceProvider(mCameraPreview?.surfaceProvider)
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun takePicture(){
        mImageCapture?.let {
            val pictureSaveFile = getPictureSaveFile()
            val outputOption = ImageCapture.OutputFileOptions.Builder(pictureSaveFile).build()
            mImageCapture?.takePicture(
                outputOption,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri ?: Uri.fromFile(pictureSaveFile)
                        Log.i(TAG,"Photo capture successed: ${savedUri}")

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            (this@CameraXSavePicLocalMainActivity as Activity).sendBroadcast(
                                Intent(Camera.ACTION_NEW_PICTURE, savedUri)
                            )
                        }

                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(
                            this@CameraXSavePicLocalMainActivity,
                            arrayOf(savedUri.toFile().absolutePath),
                            arrayOf(mimeType)){
                                _,uri -> Log.d(TAG,"Image capture scanned into media store:$uri")
                            }
                        startActivityForResult(Intent(this@CameraXSavePicLocalMainActivity,
                            CameraXSavePicLocalEditPhotoActivity::class.java).apply { this.putExtra("save_uri",savedUri.toString()) },
                            REQUESTCODE_EDIT_OK)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(TAG,"image save error")
                    }
                }
            )
            return
        }
        Log.e(TAG,"take picture error! imageCapture should not be null")
    }

    private fun getPictureSaveFile():File{
        return File(getPictureOutputDirectory(this), SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis()) + PHOTO_EXTENSION)
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

    private fun setFlashMode(flashMode:Int){
        mCameraXParams?.flashMode = flashMode
        mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.postValue(mCameraXParams)
    }

    private fun setLenFacing(lensFacing: Int){
        mCameraXParams?.lenFace = lensFacing
        mCameraXSavePicLocalModel.mCameraXSavePicLocalLiveData.postValue(mCameraXParams)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initCameraListener() {
        val zoomState: LiveData<ZoomState> = mCameraInfo!!.zoomState
        val cameraXPreviewViewTouchListener = CameraXPreviewViewTouchListener(this)

        cameraXPreviewViewTouchListener.setCustomTouchListener(object :
            CameraXPreviewViewTouchListener.CustomTouchListener {
            // 放大缩小操作
            override fun zoom(delta: Float) {
                Log.d(TAG, "缩放")
                zoomState.value?.let {
                    val currentZoomRatio = it.zoomRatio
                    mCameraControl!!.setZoomRatio(currentZoomRatio * delta)
                }
            }

            // 点击操作
            override fun click(x: Float, y: Float) {
                Log.d(TAG, "单击")
                val factory = mCameraPreview?.meteringPointFactory
                // 设置对焦位置
                val point = factory?.createPoint(x, y)
                val action = FocusMeteringAction.Builder(point!!, FocusMeteringAction.FLAG_AF)
                    // 3秒内自动调用取消对焦
                    .setAutoCancelDuration(3, TimeUnit.SECONDS)
                    .build()
                // 执行对焦
                focusView!!.startFocus(Point(x.toInt(), y.toInt()))
                val future: ListenableFuture<*> = mCameraControl!!.startFocusAndMetering(action)
                future.addListener({
                    try {
                        // 获取对焦结果
                        val result = future.get() as FocusMeteringResult
                        if (result.isFocusSuccessful) {
                            focusView!!.onFocusSuccess()
                        } else {
                            focusView!!.onFocusFailed()
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e(TAG, e.toString())
                    }
                }, ContextCompat.getMainExecutor(this@CameraXSavePicLocalMainActivity))
            }

            // 双击操作
            override fun doubleClick(x: Float, y: Float) {
                Log.d(TAG, "双击")
                // 双击放大缩小
                val currentZoomRatio = zoomState.value!!.zoomRatio
                if (currentZoomRatio > zoomState.value!!.minZoomRatio) {
                    mCameraControl!!.setLinearZoom(0f)
                } else {
                    mCameraControl!!.setLinearZoom(0.5f)
                }
            }

            override fun longPress(x: Float, y: Float) {
                Log.d(TAG, "长按")
            }
        })
        // 添加监听事件
        mCameraPreview?.setOnTouchListener(cameraXPreviewViewTouchListener)
    }

}