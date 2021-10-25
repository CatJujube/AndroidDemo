package com.jube.androiddemo.Service.Camera

import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import com.jube.androiddemo.R
import com.jube.androiddemo.Service.ServiceMainActivity
import com.jube.androiddemo.Widgets.ListFragment.ListFragment
import com.jube.androiddemo.Widgets.ListFragment.ListFragmentItem

class CameraMainFragment:ListFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(ServiceMainActivity.TAG,"CameraMainFragment")
        clear()
        add(ListFragmentItem("Camera2",object:ListFragmentItem.IListFragmentCallback{
            override fun onClick() {
                Log.i(ServiceMainActivity.TAG,"CameraMainFragment onClick()")
                view?.let { Navigation.findNavController(it).navigate(R.id.camera2MainFragment) }
            }}))
    }
}