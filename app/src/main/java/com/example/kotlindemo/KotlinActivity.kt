package com.example.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        GlobalScope.launch (Dispatchers.Main ) {
            CoroutineDemo.runSuspend()
        }
    }
}