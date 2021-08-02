package com.example.androiddemo.Criminallntent
import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Point

class PictureUtil {
    companion object{
        fun getScaledBitmap(path:String, activity: Activity): Bitmap? {
            val size = Point()
            activity.windowManager.defaultDisplay.getSize(size)
            return getScaledBitmap(path,size.x,size.y)
        }

        fun getScaledBitmap(picturePath: String?, width: Int, height: Int): Bitmap? {
            // Get the dimensions of the bitmap
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(picturePath, bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int
            scaleFactor = if (width > 0 && height > 0) {
                Math.min(photoW / width, photoH / height)
            } else {
                1
            }

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor
            bmOptions.inPurgeable = true
            return BitmapFactory.decodeFile(picturePath, bmOptions)
        }
    }
}