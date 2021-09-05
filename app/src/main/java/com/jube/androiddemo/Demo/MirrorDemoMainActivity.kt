package com.jube.androiddemo.Demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.jube.androiddemo.R
import java.io.ByteArrayOutputStream

class MirrorDemoMainActivity : AppCompatActivity() {
    companion object {
        fun BitmapToBytes(bm: Bitmap): ByteArray? {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return baos.toByteArray()
        }

        fun BytesToBitmap(bytes: ByteArray?): Bitmap? {
            return if (bytes != null) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } else null
        }

        fun RotateImage(byteArray: ByteArray?): ByteArray? {
            val bitmap = BytesToBitmap(byteArray)
            val mat = Matrix()
            mat.postScale(-1F, 1F)
            val mirroredBitmap = bitmap?.let { Bitmap.createBitmap(it, 0, 0, bitmap.width, bitmap.height, mat, true) }
            return mirroredBitmap?.let { BitmapToBytes(it) }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mirror_demo_main)
        val imageView = findViewById<ImageView>(R.id.image_normal)
        val imageView2 = findViewById<ImageView>(R.id.image_mirrored)
        val  bitmap: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.img);// 将图片读取到bitmap中
        imageView.setImageBitmap(bitmap)
        imageView2.setImageBitmap(mirror2(bitmap))
    }

    private fun mirror(bitmap: Bitmap): Bitmap? {
        val mat = Matrix()
        mat.postScale(-1F, 1F)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, mat, true)
    }

    private fun mirror2(bitmap: Bitmap): Bitmap? {
        val byteArray = BitmapToBytes(bitmap)
        val mirroredByteArray = RotateImage(byteArray)
        return BytesToBitmap(mirroredByteArray)
    }
}