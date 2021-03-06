package com.jube.androiddemo.PhotoGallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jube.androiddemo.R

class PhotoGalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        val isFragmentContainerEmpty = savedInstanceState == null
        if(isFragmentContainerEmpty){
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,PhotoGalleryFragment.newInstance()).commit()
        }
    }
}