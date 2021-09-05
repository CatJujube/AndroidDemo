package com.jube.androiddemo.CameraXDemo.CameraXSavePicLocal

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture

data class CameraXSavePicLocal_Params(
    var lenFace:Int = CameraSelector.LENS_FACING_BACK,
    var flashMode: Int = ImageCapture.FLASH_MODE_ON)
