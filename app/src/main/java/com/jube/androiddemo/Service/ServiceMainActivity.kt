package com.jube.androiddemo.Service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.jube.androiddemo.R
import com.jube.androiddemo.Service.Camera.CameraMainFragment

class ServiceMainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "ServiceMainActivity_Log"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_main)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.navHostFragment).navigateUp()
}