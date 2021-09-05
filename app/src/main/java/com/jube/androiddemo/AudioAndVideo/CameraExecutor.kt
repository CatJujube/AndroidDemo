package com.tencent.wework.msg.controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.*
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.*



class CameraExecutor(path:String) {
    private var mExtractor:MediaExtractor = MediaExtractor()
    private var mVideoTrack:Int = 0
    private var mAudioTrack:Int = 0
    private var mCurSampleTime:Long = mExtractor.sampleTime
    private var mCurSampleFlags = mExtractor.sampleFlags

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

    init {
        mExtractor.setDataSource(path)
    }

     fun getVideoFormat():MediaFormat?{
        for (i in 0 until mExtractor.trackCount) {
            val mediaFormat = mExtractor.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime!!.startsWith("video/")) {
                mVideoTrack = i
                break
            }
        }
        return if (mVideoTrack >= 0)
            mExtractor.getTrackFormat(mVideoTrack)
        else null
    }

    fun getAudioFormat(): MediaFormat? {
        for (i in 0 until mExtractor.trackCount) {
            val mediaFormat = mExtractor.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime!!.startsWith("audio/")) {
                mAudioTrack = i
                break
            }
        }
        return if (mAudioTrack >= 0) {
            mExtractor.getTrackFormat(mAudioTrack)
        } else null
    }

    /**
     * 读取音视频数据
     */
    fun readBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        selectSourceTrack()
        var readSampleCount = mExtractor!!.readSampleData(byteBuffer, 0)
        if (readSampleCount < 0) {
            return -1
        }
        mCurSampleTime = mExtractor!!.sampleTime
        mExtractor!!.advance()
        return readSampleCount
    }

    /**
     * 选择通道
     */
     fun selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor!!.selectTrack(mVideoTrack)
        } else if (mAudioTrack >= 0) {
            mExtractor!!.selectTrack(mAudioTrack)
        }
    }

    fun getCurrentTimestamp(): Long {
        return mCurSampleTime
    }

    fun getSampleFlag(): Int {
        return mCurSampleFlags
    }

    fun stop(){
        mExtractor.release()
    }
}

class MMuxer (path: String){
    private val TAG = "MMuxer"

    private var mPath: String = path

    private var mMediaMuxer: MediaMuxer? = null

    private var mVideoTrackIndex = -1
    private var mAudioTrackIndex = -1

    private var mIsAudioTrackAdd = false
    private var mIsVideoTrackAdd = false

    private var mIsStart = false

    init {
        mMediaMuxer = MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
    }

    fun addVideoTrack(mediaFormat: MediaFormat) {
        if (mMediaMuxer != null) {
            mVideoTrackIndex = try {
                mMediaMuxer!!.addTrack(mediaFormat)
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
            mIsVideoTrackAdd = true
            startMuxer()
        }
    }

    fun addAudioTrack(mediaFormat: MediaFormat) {
        if (mMediaMuxer != null) {
            mAudioTrackIndex = try {
                mMediaMuxer!!.addTrack(mediaFormat)
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
            mIsAudioTrackAdd = true
            startMuxer()
        }
    }

    /**
     *忽略音频轨道
     */
    fun setNoAudio() {
        if (mIsAudioTrackAdd) return
        mIsAudioTrackAdd = true
        startMuxer()
    }

    /**
     *忽略视频轨道
     */
    fun setNoVideo() {
        if (mIsVideoTrackAdd) return
        mIsVideoTrackAdd = true
        startMuxer()
    }

    private fun startMuxer() {
        if (mIsAudioTrackAdd && mIsVideoTrackAdd) {
            mMediaMuxer?.start()
            mIsStart = true
        }
    }

    fun writeVideoData(byteBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (mIsStart) {
            mMediaMuxer?.writeSampleData(mVideoTrackIndex, byteBuffer, bufferInfo)
        }
    }

    fun writeAudioData(byteBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (mIsStart) {
            mMediaMuxer?.writeSampleData(mAudioTrackIndex, byteBuffer, bufferInfo)
        }
    }

    fun release() {
        mIsAudioTrackAdd = false
        mIsVideoTrackAdd = false
        try {
            mMediaMuxer?.stop()
            mMediaMuxer?.release()
            mMediaMuxer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}