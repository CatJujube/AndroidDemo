package com.jube.androiddemo.Service.Camera.Camera2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.jube.androiddemo.R
import com.jube.androiddemo.Service.ServiceMainActivity
import com.jube.androiddemo.Widgets.ListFragment.ListFragment
import com.jube.androiddemo.Widgets.ListFragment.ListFragmentItem
private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
class Camera2MainFragment :ListFragment(){
    companion object{
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(ServiceMainActivity.TAG,"Camera2MainFragment")
        clear()
        add(ListFragmentItem("Camera2 Base Demo",object :ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                Log.i(ServiceMainActivity.TAG,"Camera2MainFragment onClick()")
                view?.let { Navigation.findNavController(it).navigate(R.id.camera2BaseDemoFragment) }
            }}))

        if (hasPermissions(requireContext())) {
            // If permissions have already been granted, proceed
        } else {
            // Request camera-related permissions
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        }

    }

}