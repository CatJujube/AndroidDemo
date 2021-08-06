package com.example.androiddemo.DragAndDraw

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.jar.Attributes

class BoxDrawingView(context: Context, attrs:AttributeSet) : View(context,attrs){

    companion object{
        const val TAG = "BoxDrawingView_Msg"
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val current = event?.let { PointF(event.x, it.y) }
        var action = ""
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                action = "ACTION_DOWN"
            }

            MotionEvent.ACTION_UP->{
                action = "ACTION_UP"
            }

            MotionEvent.ACTION_MOVE->{
                action = "ACTION_MOVE"
            }

            MotionEvent.ACTION_CANCEL->{
                action = "ACTION_CANCEL"
            }
        }
        Log.i(TAG,"$action at x=${current?.x}, y=${current?.y}")
        return true
    }

}