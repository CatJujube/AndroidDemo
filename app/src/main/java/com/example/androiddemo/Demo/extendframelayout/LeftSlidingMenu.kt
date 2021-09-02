package com.example.androiddemo.Demo.extendframelayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout

class LeftSlidingMenu(context: Context, attrs:AttributeSet): FrameLayout(context,attrs) {
    private lateinit var top_layout:LinearLayout
    private lateinit var bottom_layout:LinearLayout
    private var slidingFlag=false   //抽屉状态标志，默认关闭
    private var maxWidth=0  //抽屉最大宽度
    private var startPointF:PointF = PointF()
    private var startPointF2:PointF = PointF()
    private var overPointF:PointF = PointF()
    private var isFirst=true

    init {
        initView()
    }

    private fun initView(){
        //底部layout的属性设置
        bottom_layout = LinearLayout(context)
        bottom_layout.orientation = LinearLayout.VERTICAL
        //顶部layout的属性设置
        top_layout = LinearLayout(context)
        top_layout.orientation = LinearLayout.VERTICAL
        top_layout.setBackgroundColor(Color.BLUE)

    }

    public fun setBottomView(v: View){
        v.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        bottom_layout.addView(v)
    }

    public fun setTopView(v: View){
        v.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        top_layout.addView(v)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action){
            MotionEvent.ACTION_DOWN->{
                startPointF.x = ev.x
                startPointF.y = ev.y
                startPointF2.x = ev.x
                startPointF2.y = ev.y
            }
            MotionEvent.ACTION_MOVE->{
                overPointF.x = ev.x
                overPointF.y = ev.y

                var disx:Float = overPointF.x-startPointF.x
                var disy:Float = overPointF.y-startPointF.y

                //根据正余弦定理来判断  水平滑动或者是垂直滑动，
                //也就是判断滑动的倾斜度，倾斜度越大，划不动，小则可以拉动抽屉，以y轴为原点；
                if (Math.abs(disx)/2-Math.abs(disy)>0) {
                    Log.e("tag","11111");
                }else {//垂直状态--》抽屉应该关闭
                    // 通过抽屉的开关来判断上层可否移动
                    return if (!slidingFlag) super.dispatchTouchEvent(ev) else true
                }

                when{
                    disx > 0 -> {
                        //大于0  从左往右滑动
                        //获取到top_layout的属性
                        var topParames:FrameLayout.LayoutParams = top_layout.layoutParams as FrameLayout.LayoutParams
                        //滑动距离超过最大边距，将最大边距设置给滑动距离
                        if(topParames.leftMargin >= maxWidth){
                            disx = maxWidth.toFloat()
                            slidingFlag=true
                        }
                        topParames.leftMargin = disx.toInt()
                        topParames.rightMargin = -disx.toInt()
                        top_layout.layoutParams = topParames
                    }
                    disy < 0 -> {
                        var topParames:FrameLayout.LayoutParams = top_layout.layoutParams as FrameLayout.LayoutParams
                        if(topParames.leftMargin<=0){
                            disx= 0F
                            slidingFlag=false
                        }
                        topParames.leftMargin=(topParames.leftMargin-Math.abs(disx)).toInt()
                        topParames.rightMargin=-topParames.leftMargin
                        top_layout.layoutParams = topParames
                        startPointF.x = overPointF.x
                    }
                }
                requestLayout()
                return true
            }
            MotionEvent.ACTION_UP->{
                //区分是点击还是滑动

                //区分是点击还是滑动
                val disX = Math.abs(ev.x - startPointF2.x).toInt()
                if (disX > 10) {
                    //以底部linear的宽度的一半为分割线，超过分割线，抽屉自动打开或关闭
                    val topparms = top_layout.layoutParams as LayoutParams
                    if (topparms.leftMargin > maxWidth / 2) {
                        topparms.leftMargin = maxWidth
                        topparms.rightMargin = - maxWidth
                        slidingFlag = true
                    } else {
                        //抽屉关闭
                        topparms.leftMargin = 0
                        topparms.rightMargin = 0
                        slidingFlag = false
                    }
                    top_layout.layoutParams = topparms
                    requestLayout()
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(isFirst){
            //将父控件的宽缩小十分之三就是抽屉最大的宽度
            maxWidth = measuredWidth*0.7.toInt()
            //bootom_layout就是抽屉界面
            bottom_layout.layoutParams=FrameLayout.LayoutParams(maxWidth,FrameLayout.LayoutParams.MATCH_PARENT)
            //top_layout是 主界面
            top_layout.layoutParams=FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
            //将布局添加到自定义里面
            addView(bottom_layout)
            addView(top_layout)

        }
        isFirst=false
    }
}