package com.example.androiddemo.CameraXDemo.CameraXSavePicLocal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraXSavePicLocalViewModel :ViewModel(){
    val mCameraXSavePicLocalLiveData:MutableLiveData<CameraXSavePicLocal_Params> by lazy {
        MutableLiveData<CameraXSavePicLocal_Params>()
    }

    fun initData(){
        mCameraXSavePicLocalLiveData.value = CameraXSavePicLocal_Params()
    }

    fun updateData(params: CameraXSavePicLocal_Params){
        mCameraXSavePicLocalLiveData.value = params
    }
}