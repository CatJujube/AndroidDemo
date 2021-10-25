package com.jube.KotlinDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jube.androiddemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
//        FutureDemo.FutureCallableDemo()
        FutureDemo.ListenableFutureDemo()

        GlobalScope.launch (Dispatchers.Main ) {
//            CoroutineDemo.runSuspend()
            CoroutineDemo.runSuspendBeta()
        }
    }
}