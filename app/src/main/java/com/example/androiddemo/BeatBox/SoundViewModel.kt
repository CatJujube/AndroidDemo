package com.example.androiddemo.BeatBox

import android.util.Log
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class SoundViewModel : BaseObservable(){
    companion object{
        const val TAG = "BeatBox"
    }

    var sound:Sound? = null
    set(sound) {
        field = sound
        notifyChange()
    }

    @get:Bindable
    val title:String?
    get() = sound?.name

     fun onButtonClick(v: View?) {
        Log.i(TAG,"on click")
    }
}