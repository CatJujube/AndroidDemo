package com.jube.androiddemo.Service.Camera.Camera2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jube.androiddemo.R
import com.jube.androiddemo.Service.ServiceMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeoutException
import java.util.zip.Inflater
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Camera2BaseDemoFragment: Fragment() {
    companion object{
        const val TAG = "Camera2BaseDemoFragment_Log"
        private const val IMAGE_BUFFER_SIZE: Int = 3

        private const val IMAGE_CAPTURE_TIMEOUT_MILLIS: Long = 5000
        const val FLAGS_FULLSCREEN =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        /** Milliseconds used for UI animations */
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L

        data class CombinedCaptureResult(
            val image: Image,
            val metadata: CaptureResult,
            val orientation: Int,
            val format: Int
        ) : Closeable {
            override fun close() = image.close()
        }

        private fun createFile(context: Context, extension: String): File {
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
            return File(context.filesDir, "IMG_${sdf.format(Date())}.$extension")
        }
    }

    private val cameraManager: CameraManager by lazy {
        val context = requireContext().applicationContext
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(cameraId)
    }
    private lateinit var imageReader: ImageReader
    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val imageReaderThread = HandlerThread("imageReaderThread").apply { start() }
    private val imageReaderHandler = Handler(imageReaderThread.looper)
    private lateinit var camera: CameraDevice
    private lateinit var session: CameraCaptureSession
    private lateinit var relativeOrientation: OrientationLiveData
    private lateinit var cameraId:String
    private var pixelFormat:Int=0
    private var mCameraFacing = CameraCharacteristics.LENS_FACING_BACK        //默认使用后置摄像头



    private var mCaptureButton:ImageButton? = null
    private var mViewFinder:AutoFitSurfaceView? = null
    private var overlay:View?=null
    private val animationTask: Runnable by lazy {
        Runnable {
            // Flash white animation
            overlay?.background = Color.argb(150, 255, 255, 255).toDrawable()
            // Wait for ANIMATION_FAST_MILLIS
            overlay?.postDelayed({
                // Remove white flash animation
                overlay?.background = null
            }, ANIMATION_FAST_MILLIS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.camera2_base_demo_fragment, container, false)
        mCaptureButton = view.findViewById(R.id.capture_button)
        mViewFinder = view.findViewById(R.id.view_finder)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCamera()
        mCaptureButton?.setOnApplyWindowInsetsListener { v, insets ->
            v.translationX = (-insets.systemWindowInsetRight).toFloat()
            v.translationY = (-insets.systemWindowInsetBottom).toFloat()
            insets.consumeSystemWindowInsets()
        }
        mViewFinder?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceDestroyed(holder: SurfaceHolder) = Unit

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = Unit

            override fun surfaceCreated(holder: SurfaceHolder) {
                // Selects appropriate preview size and configures view finder
                val previewSize = mViewFinder?.display?.let {
                    getPreviewOutputSize(
                        it,
                        characteristics,
                        SurfaceHolder::class.java
                    )
                }
                Log.d(TAG, "View finder size: ${mViewFinder?.width} x ${mViewFinder?.height}")
                Log.d(TAG, "Selected preview size: $previewSize")
                mViewFinder?.setAspectRatio(
                    previewSize!!.width,
                    previewSize!!.height
                )

                // To ensure that size is set, initialize camera in the view's thread
                view.post { initializeCamera() }
            }
        })
    }

    private fun initializeCamera() = lifecycleScope.launch(Dispatchers.Main) {
        camera = openCamera(cameraManager, cameraId, cameraHandler)

        val size = characteristics.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            .getOutputSizes(pixelFormat).maxByOrNull { it.height * it.width }!!
        imageReader = ImageReader.newInstance(
            size.width, size.height, pixelFormat, IMAGE_BUFFER_SIZE)

        val targets = listOf(mViewFinder?.holder?.surface, imageReader.surface)

        session = createCaptureSession(camera, targets as List<Surface>, cameraHandler)

        val captureRequest = camera.createCaptureRequest(
            CameraDevice.TEMPLATE_PREVIEW).apply { mViewFinder?.holder?.surface?.let { addTarget(it) } }

        session.setRepeatingRequest(captureRequest.build(), null, cameraHandler)

        mCaptureButton?.setOnClickListener {
            Log.i(TAG,"mCaptureButton is clicked!")

            it.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                takePhoto().use { result ->
                    Log.d(TAG, "Result received: $result")

                    val output = saveResult(result)
                    Log.d(TAG, "Image saved: ${output.absolutePath}")

                    if (output.extension == "jpg") {
                        val exif = ExifInterface(output.absolutePath)
                        exif.setAttribute(
                            ExifInterface.TAG_ORIENTATION, result.orientation.toString())
                        exif.saveAttributes()
                        Log.d(TAG, "EXIF metadata saved: ${output.absolutePath}")
                    }

//                    lifecycleScope.launch(Dispatchers.Main) {
//                        navController.navigate(CameraFragmentDirections
//                            .actionCameraToJpegViewer(output.absolutePath)
//                            .setOrientation(result.orientation)
//                            .setDepth(
//                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
//                                    result.format == ImageFormat.DEPTH_JPEG))
//                    }
                }

                it.post { it.isEnabled = true }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun openCamera(
        manager: CameraManager,
        cameraId: String,
        handler: Handler? = null
    ): CameraDevice = suspendCancellableCoroutine { cont ->
        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(device: CameraDevice) = cont.resume(device)

            override fun onDisconnected(device: CameraDevice) {
                Log.w(TAG, "Camera $cameraId has been disconnected")
                requireActivity().finish()
            }

            override fun onError(device: CameraDevice, error: Int) {
                val msg = when (error) {
                    ERROR_CAMERA_DEVICE -> "Fatal (device)"
                    ERROR_CAMERA_DISABLED -> "Device policy"
                    ERROR_CAMERA_IN_USE -> "Camera in use"
                    ERROR_CAMERA_SERVICE -> "Fatal (service)"
                    ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                    else -> "Unknown"
                }
                val exc = RuntimeException("Camera $cameraId error: ($error) $msg")
                Log.e(TAG, exc.message, exc)
                if (cont.isActive) cont.resumeWithException(exc)
            }
        }, handler)
    }

    private suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCoroutine { cont ->
        device.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {

            override fun onConfigured(session: CameraCaptureSession) = cont.resume(session)

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exc = RuntimeException("Camera ${device.id} session configuration failed")
                Log.e(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }, handler)
    }

    private suspend fun takePhoto():
            CombinedCaptureResult = suspendCoroutine { cont ->

        @Suppress("ControlFlowWithEmptyBody")
        while (imageReader.acquireNextImage() != null) {
        }

        val imageQueue = ArrayBlockingQueue<Image>(IMAGE_BUFFER_SIZE)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireNextImage()
            Log.d(TAG, "Image available in queue: ${image.timestamp}")
            imageQueue.add(image)
        }, imageReaderHandler)

        val captureRequest = session.device.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE).apply { addTarget(imageReader.surface) }
        session.capture(captureRequest.build(), object : CameraCaptureSession.CaptureCallback() {

            override fun onCaptureStarted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                timestamp: Long,
                frameNumber: Long) {
                super.onCaptureStarted(session, request, timestamp, frameNumber)
                mViewFinder?.post(animationTask)
            }

            override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult) {
                super.onCaptureCompleted(session, request, result)
                val resultTimestamp = result.get(CaptureResult.SENSOR_TIMESTAMP)
                Log.d(TAG, "Capture result received: $resultTimestamp")

                val exc = TimeoutException("Image dequeuing took too long")
                val timeoutRunnable = Runnable { cont.resumeWithException(exc) }
                imageReaderHandler.postDelayed(timeoutRunnable, IMAGE_CAPTURE_TIMEOUT_MILLIS)

                @Suppress("BlockingMethodInNonBlockingContext")
                lifecycleScope.launch(cont.context) {
                    while (true) {
                        val image = imageQueue.take()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                            image.format != ImageFormat.DEPTH_JPEG &&
                            image.timestamp != resultTimestamp) continue
                        Log.d(TAG, "Matching image dequeued: ${image.timestamp}")

                        imageReaderHandler.removeCallbacks(timeoutRunnable)
                        imageReader.setOnImageAvailableListener(null, null)

                        while (imageQueue.size > 0) {
                            imageQueue.take().close()
                        }

                        val rotation = relativeOrientation.value ?: 0
                        val mirrored = characteristics.get(CameraCharacteristics.LENS_FACING) ==
                                CameraCharacteristics.LENS_FACING_FRONT
                        val exifOrientation = computeExifOrientation(rotation, mirrored)

                        cont.resume(CombinedCaptureResult(
                            image, result, exifOrientation, imageReader.imageFormat))
                    }
                }
            }
        }, cameraHandler)
    }

    private suspend fun saveResult(result: CombinedCaptureResult): File = suspendCoroutine { cont ->
        when (result.format) {

            // When the format is JPEG or DEPTH JPEG we can simply save the bytes as-is
            ImageFormat.JPEG, ImageFormat.DEPTH_JPEG -> {
                val buffer = result.image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining()).apply { buffer.get(this) }
                try {
                    val output = createFile(requireContext(), "jpg")
                    FileOutputStream(output).use { it.write(bytes) }
                    cont.resume(output)
                } catch (exc: IOException) {
                    Log.e(TAG, "Unable to write JPEG image to file", exc)
                    cont.resumeWithException(exc)
                }
            }

            // When the format is RAW we use the DngCreator utility library
            ImageFormat.RAW_SENSOR -> {
                val dngCreator = DngCreator(characteristics, result.metadata)
                try {
                    val output = createFile(requireContext(), "dng")
                    FileOutputStream(output).use { dngCreator.writeImage(it, result.image) }
                    cont.resume(output)
                } catch (exc: IOException) {
                    Log.e(TAG, "Unable to write DNG image to file", exc)
                    cont.resumeWithException(exc)
                }
            }

            // No other formats are supported by this sample
            else -> {
                val exc = RuntimeException("Unknown image format: ${result.image.format}")
                Log.e(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            camera.close()
        } catch (exc: Throwable) {
            Log.e(TAG, "Error closing camera", exc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
        imageReaderThread.quitSafely()
    }

    private fun setUpCamera(){
        val cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraList = enumerateCameras(cameraManager)
        for(item:FormatItem in cameraList){
            Log.i(TAG,"camera title ${item.title}")
        }
    }

    @SuppressLint("InlinedApi")
    private fun enumerateCameras(cameraManager: CameraManager): List<FormatItem> {
        val availableCameras: MutableList<FormatItem> = mutableListOf()

        // Get list of all compatible cameras
//        val cameraIds = cameraManager.cameraIdList.filter {
//            val characteristics = cameraManager.getCameraCharacteristics(it)
//            val capabilities = characteristics.get(
//                CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
//            capabilities?.contains(
//                CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) ?: false
//        }
        val cameraIds = cameraManager.cameraIdList
        cameraIds.forEach { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if(facing == mCameraFacing){
                cameraId = id
            }
            pixelFormat = ImageFormat.JPEG
            val orientation = lensOrientationString(characteristics.get(CameraCharacteristics.LENS_FACING)!!)

            val capabilities = characteristics.get(
                CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)!!
            val outputFormats = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.outputFormats

            availableCameras.add(FormatItem(
                "$orientation JPEG ($id)", id, ImageFormat.JPEG))

            if (capabilities.contains(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW) &&
                outputFormats.contains(ImageFormat.RAW_SENSOR)) {
                availableCameras.add(FormatItem(
                    "$orientation RAW ($id)", id, ImageFormat.RAW_SENSOR))
            }

            if (capabilities.contains(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT) &&
                outputFormats.contains(ImageFormat.DEPTH_JPEG)) {
                availableCameras.add(FormatItem(
                    "$orientation DEPTH ($id)", id, ImageFormat.DEPTH_JPEG))
            }
        }
        return availableCameras
    }

    private fun lensOrientationString(value: Int) = when(value) {
        CameraCharacteristics.LENS_FACING_BACK -> "Back"
        CameraCharacteristics.LENS_FACING_FRONT -> "Front"
        CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
        else -> "Unknown"
    }

    private data class FormatItem(val title: String, val cameraId: String, val format: Int)
}