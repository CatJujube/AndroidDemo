package com.example.cameraxex

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.Camera
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.cameraxex.FocusModule.CameraXPreviewViewTouchListener
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CameraXExMoudle(context: Context, params: CameraXEx_Params) {
    companion object {
        const val TAG = "CameraXExUtil_LogMsg"
        private const val PERMISSIONS_REQUEST_CODE = 10
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
        const val PHOTO_EXTENSION = ".jpg"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUESTCODE_EDIT_OK = 100
    }

    private var context:Context = context
    private var params: CameraXEx_Params = params


    fun initCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
//                    val imageCapture = cameraxexParams.imageCapture ?: Log.e(TAG, "CameraXEx_Params.imageCapture should not be null!")
                val imageCapture = params.imageCapture ?: throw IllegalArgumentException("CameraXEx_Params.imageCapture should not be null!")
                imageCapture.flashMode = params.flashMode
                val cameraSelector = CameraSelector.Builder().requireLensFacing(params.lenFacing).build()
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(context as LifecycleOwner,cameraSelector,preview,imageCapture)
                params.cameraControl = camera.cameraControl
                params.cameraInfo = camera.cameraInfo
                loadFocusModule()
                preview.setSurfaceProvider(params.cameraPreview?.surfaceProvider)
            },ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(callback:OnTackPhotoCallback){
        params.imageCapture?.let {
            val pictureSaveFile = getPictureSaveFile(context)
            val outputOption = ImageCapture.OutputFileOptions.Builder(pictureSaveFile).build()
            params.imageCapture?.takePicture(
                outputOption,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri ?: Uri.fromFile(pictureSaveFile)
                        Log.i(TAG,"Photo capture successed: ${savedUri}")

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            (context as Activity).sendBroadcast(
                                Intent(Camera.ACTION_NEW_PICTURE, savedUri)
                            )
                        }

                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(
                            context,
                            arrayOf(savedUri.toFile().absolutePath),
                            arrayOf(mimeType)){
                                _,uri -> Log.d(TAG,"Image capture scanned into media store:$uri")
                        }
                        callback.onSuccess(savedUri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        callback.onFail()
                        Log.e(TAG,"image save error")
                    }
                }
            )
            return
        }
        Log.e(TAG,"take picture error! imageCapture should not be null")
    }

    fun setFlashMode(flashMode: Int){
        params.flashMode = flashMode
        initCamera()
    }

    fun setLenFacing(lensFacing: Int){
        params.lenFacing = lensFacing
        initCamera()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun loadFocusModule(){
        val zoomState: LiveData<ZoomState> = params.cameraInfo!!.zoomState
        val cameraXPreviewViewTouchListener = CameraXPreviewViewTouchListener(context)

        cameraXPreviewViewTouchListener.setCustomTouchListener(object :
            CameraXPreviewViewTouchListener.CustomTouchListener {
            // 放大缩小操作
            override fun zoom(delta: Float) {
                Log.d(TAG, "缩放")
                zoomState.value?.let {
                    val currentZoomRatio = it.zoomRatio
                    params.cameraControl!!.setZoomRatio(currentZoomRatio * delta)
                }
            }

            // 点击操作
            override fun click(x: Float, y: Float) {
                Log.d(TAG, "单击")
                val factory = params.cameraPreview?.meteringPointFactory
                // 设置对焦位置
                val point = factory?.createPoint(x, y)
                val action = FocusMeteringAction.Builder(point!!, FocusMeteringAction.FLAG_AF)
                    // 3秒内自动调用取消对焦
                    .setAutoCancelDuration(3, TimeUnit.SECONDS)
                    .build()
                // 执行对焦
                params.focusView!!.startFocus(Point(x.toInt(), y.toInt()))
                val future: ListenableFuture<*> = params.cameraControl!!.startFocusAndMetering(action)
                future.addListener({
                    try {
                        // 获取对焦结果
                        val result = future.get() as FocusMeteringResult
                        if (result.isFocusSuccessful) {
                            params.focusView!!.onFocusSuccess()
                        } else {
                            params.focusView!!.onFocusFailed()
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e(TAG, e.toString())
                    }
                }, ContextCompat.getMainExecutor(context))
            }

            // 双击操作
            override fun doubleClick(x: Float, y: Float) {
                Log.d(TAG, "双击")
                // 双击放大缩小
                val currentZoomRatio = zoomState.value!!.zoomRatio
                if (currentZoomRatio > zoomState.value!!.minZoomRatio) {
                    params.cameraControl!!.setLinearZoom(0f)
                } else {
                    params.cameraControl!!.setLinearZoom(0.5f)
                }
            }

            override fun longPress(x: Float, y: Float) {
                Log.d(TAG, "长按")
            }
        })
        // 添加监听事件
        params.cameraPreview?.setOnTouchListener(cameraXPreviewViewTouchListener)
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

    interface OnTackPhotoCallback{
        fun onSuccess(uri: Uri)
        fun onFail()
    }
}