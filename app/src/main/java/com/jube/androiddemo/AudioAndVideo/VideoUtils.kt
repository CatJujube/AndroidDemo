package com.jube.androiddemo.AudioAndVideo

import android.media.MediaCodec
import com.tencent.wework.msg.controller.CameraExecutor
import com.tencent.wework.msg.controller.MMuxer
import java.nio.ByteBuffer

class VideoUtils(srcPath: String, mirroredPath:String) {
    private val TAG = "VideoMirrorUtil"

    //初始化音视频分离器
    private val mAExtractor: CameraExecutor = CameraExecutor(srcPath)
    private val mVExtractor: CameraExecutor = CameraExecutor(srcPath)

    //初始化封装器
    private val mMuxer: MMuxer = MMuxer(mirroredPath)

    /**
     *启动重封装
     */
    fun start() {
        val audioFormat = mAExtractor.getAudioFormat()
        val videoFormat = mVExtractor.getVideoFormat()

        //判断是否有音频数据，没有音频数据则告诉封装器，忽略音频轨道
        if (audioFormat != null) {
            mMuxer.addAudioTrack(audioFormat)
        } else {
            mMuxer.setNoAudio()
        }
        //判断是否有视频数据，没有音频数据则告诉封装器，忽略视频轨道
        if (videoFormat != null) {
            mMuxer.addVideoTrack(videoFormat)
        } else {
            mMuxer.setNoVideo()
        }

        //启动线程
        Thread {
            val buffer = ByteBuffer.allocate(500 * 1024)
            val bufferInfo = MediaCodec.BufferInfo()

            //音频数据分离和写入
            if (audioFormat != null) {
                var size = mAExtractor.readBuffer(buffer)
                while (size > 0) {
                    bufferInfo.set(0, size, mAExtractor.getCurrentTimestamp(),
                        mAExtractor.getSampleFlag())

                    mMuxer.writeAudioData(buffer, bufferInfo)

                    size = mAExtractor.readBuffer(buffer)
                }
            }

            //视频数据分离和写入
            if (videoFormat != null) {
                var size = mVExtractor.readBuffer(buffer)

                while (size > 0) {
                    var mirroredBuffer: ByteBuffer? = null
                    if (CameraExecutor.RotateImage(buffer.array())!=null) mirroredBuffer = ByteBuffer.wrap(
                        CameraExecutor.RotateImage(buffer.array())!!)    //镜像翻转
                    bufferInfo.set(0, size, mVExtractor.getCurrentTimestamp(),
                        mVExtractor.getSampleFlag())

                    mMuxer.writeVideoData(mirroredBuffer?:buffer, bufferInfo)

                    size = mVExtractor.readBuffer(buffer)
                }
            }
            mAExtractor.stop()
            mVExtractor.stop()
            mMuxer.release()
        }.start()
    }
}