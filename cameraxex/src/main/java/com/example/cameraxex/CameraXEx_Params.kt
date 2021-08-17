package com.example.cameraxex

import androidx.annotation.NonNull
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import com.example.cameraxex.FocusModule.CameraFocusView

data class CameraXEx_Params (
    var lenFacing:Int = CameraSelector.LENS_FACING_BACK,
    var flashMode:Int = ImageCapture.FLASH_MODE_ON,
    @NonNull var imageCapture:ImageCapture? = null,
    @NonNull var cameraControl: CameraControl? = null,
    @NonNull var cameraInfo:CameraInfo? = null,
    @NonNull var cameraPreview:PreviewView ?= null,  //相机预览view
    @NonNull var focusView: CameraFocusView?= null   //聚焦模块view
)