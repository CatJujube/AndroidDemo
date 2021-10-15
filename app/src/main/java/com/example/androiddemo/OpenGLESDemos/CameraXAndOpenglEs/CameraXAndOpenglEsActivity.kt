package com.example.androiddemo.OpenGLESDemos.CameraXAndOpenglEs

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.lifecycle.ProcessCameraProvider

import android.content.pm.PackageManager

import android.widget.Toast
import androidx.annotation.NonNull
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.jube.androiddemo.R
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CameraXAndOpenglEsActivity : AppCompatActivity() {
    private var cameraPreview: GLCameraView? = null

    companion object {
//        init {
////            System.loadLibrary("native-lib")
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_xand_opengl_es)
        cameraPreview = findViewById(R.id.camera_preview_2)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(Manifest.permission.CAMERA), 100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "没有相机权限", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )  === PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val executor: Executor = Executors.newSingleThreadExecutor()
        val processCameraProvider: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(this)
        processCameraProvider.addListener(Runnable {
            try {
                val cameraProvider: ProcessCameraProvider = processCameraProvider.get()
                val preview: Preview = Preview.Builder()
                    .build()
                cameraPreview!!.attachPreview(preview)
                cameraProvider.unbindAll()
                val bindToLifecycle = cameraProvider.bindToLifecycle(
                    this@CameraXAndOpenglEsActivity,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
                )
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    external fun stringFromJNI(): String?
}
