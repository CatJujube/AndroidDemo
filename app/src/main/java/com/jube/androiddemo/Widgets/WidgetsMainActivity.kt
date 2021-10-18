package com.jube.androiddemo.Widgets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.CameraXDemo.CameraXMainActivity
import com.jube.androiddemo.HomePageAdapter
import com.jube.androiddemo.HomePageItem
import com.jube.androiddemo.MainActivity
import com.jube.androiddemo.R

class WidgetsMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widgets_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,WidgetsMainFragment())
    }

}