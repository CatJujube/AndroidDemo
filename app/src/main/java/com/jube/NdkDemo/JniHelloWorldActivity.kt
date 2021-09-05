package com.jube.NdkDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jube.androiddemo.R

class JniHelloWorldActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni_hello_world)
        val textView = findViewById<TextView>(R.id.jni_helloworld_textview)
        textView.text = HelloJni().hello()
    }
}