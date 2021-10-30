package com.jube.androiddemo.Service.Camera.Camera2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.jube.androiddemo.R

class PhotoGallary:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fargment_camera2_base_demo_photo_gallary,container,false)
        val imageView = view.findViewById<ImageView>(R.id.photoContainer)
        return view;
    }
}