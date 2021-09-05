package com.jube.androiddemo.CameraXDemo

import android.content.Context
import com.jube.androiddemo.R
import java.io.File


class CameraUtil {
    companion object {
        fun getOutputFile(context: Context): File {
            val appContext: Context = context.applicationContext
            val mediaDirs: Array<File?> = context.externalMediaDirs
            if (mediaDirs.isNotEmpty() && mediaDirs[0] != null) {
                val appMediaDir =
                    File(mediaDirs[0], appContext.resources.getString(R.string.app_name))
                appMediaDir.mkdirs()
                return appMediaDir
            }
            return appContext.filesDir
        }

        fun createPhotoFile(outputFile: File?): File {
            return File(outputFile, String.format("%s%s", System.currentTimeMillis(), ".png"))
        }
    }
}
