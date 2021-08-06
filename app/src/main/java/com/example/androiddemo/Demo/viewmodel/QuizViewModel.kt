package com.example.androiddemo.Demo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class QuizViewModel:ViewModel() {
     var mButtonNames = arrayListOf<String>("按钮1",
        "按钮2",
        "按钮3",
        "按钮4",
        "按钮5")

    companion object{
        const val TAG = "ViewModel Demo"
    }

    init {
        Log.d(TAG,"ViewModel instance created!")
    }


    /**
     * onCleared()函数的调用恰好在ViewModel被销毁之前。这里适合做一些善后清理工作，比如解绑某个数据源
     * **/
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"ViewModel instance about to be destroyed!")
    }

    fun changeButtonName(index:Int, name:String){
        mButtonNames[index] = name
    }
}