package com.example.androiddemo.OpenGLESDemos.CameraXAndOpenglEs

import android.graphics.SurfaceTexture

import android.opengl.GLES11Ext

import android.opengl.GLES20

import javax.microedition.khronos.opengles.GL10


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import android.opengl.EGLConfig

import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import androidx.annotation.NonNull
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.core.util.Consumer
import com.jube.androiddemo.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class GLCameraView(context: Context?, attrs: AttributeSet?) :
    GLSurfaceView(context, attrs), GLSurfaceView.Renderer, OnFrameAvailableListener {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private var textureId = 0
    private var surfaceTexture: SurfaceTexture? = null
    private var vPosition = 0
    private var vCoord = 0
    private var programId = 0
    private var textureMatrixId = 0
    private val textureMatrix = FloatArray(16)
    protected var mGLVertexBuffer: FloatBuffer? = null
    protected var mGLTextureBuffer: FloatBuffer? = null

    constructor(context: Context?) : this(context, null) {}

    @SuppressLint("UnsafeExperimentalUsageError")
    fun attachPreview(preview: Preview) {
        preview.setSurfaceProvider(object : Preview.SurfaceProvider {
            override fun onSurfaceRequested(@NonNull request: SurfaceRequest) {
                val surface = Surface(surfaceTexture)
                request.provideSurface(
                    surface,
                    executor,
                    object : Consumer<SurfaceRequest.Result?> {
                        override fun accept(result: SurfaceRequest.Result?) {
                            surface.release()
                            surfaceTexture!!.release()
                            Log.v(LOG_TAG, "--accept------")
                        }
                    })
            }
        })
    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        val ids = IntArray(1)

        // OpenGL??????
        GLES20.glGenTextures(1, ids, 0)
        textureId = ids[0]
        surfaceTexture = SurfaceTexture(textureId)
        surfaceTexture!!.setOnFrameAvailableListener { surfaceTexture: SurfaceTexture ->
            onFrameAvailable(
                surfaceTexture
            )
        }
        val vertexShader: String? = OpenGLUtils.readRawTextFile(context, R.raw.camera_vertex)
        val fragmentShader: String? = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag)
        programId = OpenGLUtils.loadProgram(vertexShader, fragmentShader)
        vPosition = GLES20.glGetAttribLocation(programId, "vPosition")
        vCoord = GLES20.glGetAttribLocation(programId, "vCoord")
        textureMatrixId = GLES20.glGetUniformLocation(programId, "textureMatrix")

        // 4???????????????????????????????????????????????????????????????4?????????
        mGLVertexBuffer =
            ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer()
        mGLVertexBuffer?.clear()
        // ????????????
        val VERTEX = floatArrayOf(
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
        )
        mGLVertexBuffer?.put(VERTEX)

        // ????????????
        mGLTextureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mGLTextureBuffer?.clear()

        // ?????????????????????????????????????????????????????????????????????????????????????????????
//        float[] TEXTURE = {
//                0.0f, 1.0f,
//                1.0f, 1.0f,
//                0.0f, 0.0f,
//                1.0f, 0.0f
//        };

        // ??????????????????????????????????????????
        val TEXTURE = floatArrayOf(
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
        )
        mGLTextureBuffer?.put(TEXTURE)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {

        // ??????
        GLES20.glClearColor(1f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // ????????????
        surfaceTexture!!.updateTexImage()
        surfaceTexture!!.getTransformMatrix(textureMatrix)
        GLES20.glUseProgram(programId)

        //????????????
        GLES20.glUniformMatrix4fv(textureMatrixId, 1, false, textureMatrix, 0)

        // ??????????????????
        mGLVertexBuffer?.position(0)
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGLVertexBuffer)
        GLES20.glEnableVertexAttribArray(vPosition)

        // ??????????????????
        mGLTextureBuffer?.position(0)
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGLTextureBuffer)
        GLES20.glEnableVertexAttribArray(vCoord)

        //????????????
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        // ????????????
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture) {
        requestRender()
    }

    companion object {
        private const val LOG_TAG = "OpenGLCameraX"
    }

    init {
        setEGLContextClientVersion(2)
        setRenderer(this)
        // ?????????????????????
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}
