package com.jube.androiddemo.Demo.extendframelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter

import android.view.View
import android.widget.ImageView
import android.widget.ListView
import com.jube.androiddemo.R


class ExtendFrameLayoutMainActivity : AppCompatActivity() {
    private var leftSlidingMenu: LeftSlidingMenu? = null

    private val list: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extend_frame_layout_main)
        initdata()
        initview()
    }

    private fun initdata() {
        for (i in 0..29) {
            list?.add("item$i")
        }
    }


    private fun initview() {
        leftSlidingMenu = findViewById<View>(R.id.mysliding_menu) as LeftSlidingMenu
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.ic_launcher_background);
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        leftSlidingMenu!!.setBottomView(imageView)
        val listView = ListView(this)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list!!)
        listView.adapter = adapter
        leftSlidingMenu!!.setTopView(listView)
    }
}