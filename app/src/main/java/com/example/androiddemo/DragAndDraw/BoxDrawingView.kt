package com.example.androiddemo.DragAndDraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.jar.Attributes

class BoxDrawingView(context: Context, attrs:AttributeSet) : View(context,attrs){

    private var currentBox:Box? = null
    private val boxen = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    companion object{
        const val TAG = "BoxDrawingView_Msg"
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val current = event?.let { PointF(event.x, it.y) }
        var action = ""
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                action = "ACTION_DOWN"
                currentBox = Box(current!!).also {
                    boxen.add(it)
                }
            }

            MotionEvent.ACTION_UP->{
                action = "ACTION_UP"
                updateCurrentBox(current!!)
                currentBox = null
            }

            MotionEvent.ACTION_MOVE->{
                action = "ACTION_MOVE"
                updateCurrentBox(current!!)
            }

            MotionEvent.ACTION_CANCEL->{
                action = "ACTION_CANCEL"
                currentBox=null
            }
        }
        Log.i(TAG,"$action at x=${current?.x}, y=${current?.y}")
        return true
    }

    private fun updateCurrentBox(current:PointF){
        currentBox?.let{
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPaint(backgroundPaint)
        boxen.forEach{
            box->canvas?.drawRect(box.left,box.top,box.right,box.bottom,boxPaint)
        }
    }

}