package com.jube.androiddemo.Service.Camera.Camera2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.jube.androiddemo.R
import com.jube.androiddemo.Service.ServiceMainActivity
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.*

class Camera2BaseDemoFragment: Fragment() {
    companion object{
        const val TAG = "Camera2BaseDemoFragment_Log"
    }

    private lateinit var mCameraManager:CameraManager
    private lateinit var mImageReader:ImageReader
    private var mPreviewSize:Size = Size(1920,1080)
    private lateinit var mImageHandler: Handler
    private val mImageAvailableListener:ImageReader.OnImageAvailableListener = object :ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader?) {
            val image: Image? = reader?.acquireNextImage()
            Log.d(TAG, "##### onFrame: " + image?.planes)
            image?.close()
        }
    }
    private val mTextureView: TextureView? = view?.findViewById(R.id.previewContainer)
    private lateinit var mCameraId:String
    private val mStateCallback:CameraDevice.StateCallback = object:CameraDevice.StateCallback(){
        override fun onOpened(camera: CameraDevice) {
            setUpImageReader()
            val imageReaderSurface:Surface = mImageReader.surface
            mCaptureRequestBuilder?.addTarget(imageReaderSurface)
            startPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
        }

        override fun onError(camera: CameraDevice, error: Int) {
        }
    }
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null
    private var mCaptureRequestBuilder:CaptureRequest.Builder? = null
    private var mCameraDevice:CameraDevice? = null
    private var mCaptureRequest:CaptureRequest? = null
    private var mPreviewSession:CameraCaptureSession? = null
    private var mSessionCaptureCallback: CameraCaptureSession.CaptureCallback? = null


    private val mTextureListener:TextureView.SurfaceTextureListener = object :TextureView.SurfaceTextureListener{
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            /**当SurfaceTexture可用的时候，设置相机参数并打开相机*/
            setUpCamera(width,height)
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(ServiceMainActivity.TAG,"Camera2BaseDemoFragment")
        return inflater.inflate(R.layout.camera2_base_demo_fragment, container, false)
    }

    /**
     * @brief 设置相机参数
     * 为了更好地预览，我们根据TextureView的尺寸设置预览尺寸，Camera2中使用CameraManager来管理摄像头
     */
    private fun setUpCamera(width:Int,height:Int){
        prepareCameraManager()
        try {
            /**遍历所有摄像头**/
            for(cameraId in mCameraManager.cameraIdList){
                val characteristics = mCameraManager.getCameraCharacteristics(cameraId)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                /**默认打开后置摄像头**/
                if(null != facing && facing == CameraCharacteristics.LENS_FACING_FRONT) continue
                /**获取StreamConfigurationMap,管理摄像头支持的所有输出格式和尺寸**/
                val map: StreamConfigurationMap? = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                /**根据TextureView的尺寸设置预览尺寸**/
//                mPreviewSize = getOptimalSize(map!!.getOutputSizes(SurfaceTexture::class.java), width, height)
                mCameraId = cameraId
                break
            }
        }catch (e:Exception){
            e.message?.let { Log.e(TAG, it) }
        }
    }

    /**
     * @brief 打开相机
     */
    private fun openCamera(){
        /**获取摄像头的CameraManager**/
        val manager:CameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        /**检查权限**/
        try{
            if(context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } !=PackageManager.PERMISSION_GRANTED){
                return
            }
            /**打开相机，第一个参数指示打开哪个摄像头，第二个参数stateCallback为相机的状态回调接口，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行**/
            manager.openCamera(mCameraId, mStateCallback, null)
        }catch (e:Exception){
            e.message?.let { Log.e(TAG, it) }
        }
    }

    /**
     * @brief 开始预览
     */

    private fun startPreview(){
        mSurfaceTexture = mTextureView?.surfaceTexture
        mSurfaceTexture?.setDefaultBufferSize(mPreviewSize.width,mPreviewSize.height)
        mSurface = Surface(mSurfaceTexture)
        try {
            mCaptureRequestBuilder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mCaptureRequestBuilder?.addTarget(mSurface!!)
            mCameraDevice?.createCaptureSession(listOf(mSurface),object :CameraCaptureSession.StateCallback(){
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        mCaptureRequest = mCaptureRequestBuilder?.build()
                        mPreviewSession = session
                        mCaptureRequest?.let { mPreviewSession!!.setRepeatingRequest(it,mSessionCaptureCallback,null) }
                    }catch (e:CameraAccessException){
                        e.message?.let { Log.e(TAG, it) }
                    }
                }
                override fun onConfigureFailed(session: CameraCaptureSession) {
                }
            },null)
        }catch (e:Exception){
            e.message?.let { Log.e(TAG, it) }
        }
    }

    private fun setUpImageReader(){
        mImageReader = ImageReader.newInstance(mPreviewSize.width,mPreviewSize.height,ImageFormat.JPEG,2)
        mImageReader.setOnImageAvailableListener(object :ImageReader.OnImageAvailableListener{
            override fun onImageAvailable(reader: ImageReader?) {
                val image:Image = reader!!.acquireLatestImage()
                val buffer:ByteBuffer = image.planes[0].buffer
                val data:ByteArray = ByteArray(buffer.remaining())
                buffer.get(data)
                image.close()
            }
        },null)
    }

    override fun onResume() {
        super.onResume()
        mTextureView?.surfaceTextureListener = mTextureListener
    }

    //获取ImageManager
    private fun prepareCameraManager(){
        mCameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }


    /**
     * @brief ImageReader是获取图像数据的重要途径，通过它可以获取到不同格式的图像数据，例如JPEG、YUV、RAW等。通过ImageReader.newInstance(int width, int height, int format, int maxImages)创建ImageReader对象，有4个参数：
     * param width：图像数据的宽度
     * param height：图像数据的高度
     * param format: format：图像数据的格式，例如ImageFormat.JPEG，ImageFormat.YUV_420_888等
     * param maxImages: 最大Image个数，Image对象池的大小，指定了能从ImageReader获取Image对象的最大值，过多获取缓冲区可能导致OOM，所以最好按照最少的需要去设置这个值
     * oth: ImageReader.OnImageAvailableListener：有新图像数据的回调
     * oth: acquireLatestImage()：从ImageReader的队列里面，获取最新的Image，删除旧的，如果没有可用的Image，返回null
     * oth: acquireNextImage()：获取下一个最新的可用Image，没有则返回null
     * oth: close()：释放与此ImageReader关联的所有资源
     * oth: getSurface()：获取为当前ImageReader生成Image的Surface
     */
    private fun prepareImageReader(){
        mImageReader = ImageReader.newInstance(mPreviewSize.width,mPreviewSize.height,ImageFormat.YUV_420_888,1)
        mImageReader.setOnImageAvailableListener(mImageAvailableListener,mImageHandler)
    }

}