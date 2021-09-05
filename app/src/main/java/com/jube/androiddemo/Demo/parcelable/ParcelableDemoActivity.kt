package com.jube.androiddemo.Demo.parcelable

import android.app.Activity
import android.os.Bundle
import com.jube.androiddemo.R

class ParcelableDemoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.parcelable_demo_page)
    }
}