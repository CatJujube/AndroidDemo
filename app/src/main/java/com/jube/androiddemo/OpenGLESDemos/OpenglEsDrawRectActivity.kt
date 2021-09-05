package com.jube.androiddemo.OpenGLESDemos

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jube.androiddemo.R
import java.nio.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import com.jube.androiddemo.OpenGLESDemos.Square.Companion.COORDS_PER_VERTEX


/**
 * @link https://blog.csdn.net/qq_32175491/article/details/79091647
 */
class OpenglEsDrawRectActivity : AppCompatActivity() {
    private var mGLSurfaceView:DrawGlSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_es_draw_rect)
        mGLSurfaceView = DrawGlSurfaceView(this)
        setContentView(mGLSurfaceView)
    }
}

//设置GLSurfaceView，并设置渲染器
class DrawGlSurfaceView(context: Context): GLSurfaceView(context) {
    private val mRenderer:DrawGlRender

    init {
        //设置OpenGL版本为2.0
        setEGLContextClientVersion(2)
        this.mRenderer = DrawGlRender()
        setRenderer(mRenderer)
    }
}

//实现渲染器接口
class DrawGlRender: GLSurfaceView.Renderer{
    private var mTriangle:Triangle? = null
    private var mSquare:Square? = null

    companion object{
        fun loadShader(type: Int, shaderCode: String?): Int {

            // 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
            // 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
            val shader = GLES20.glCreateShader(type)
            // 添加上面编写的着色器代码并编译它
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景帧颜色为黑色
        GLES20.glClearColor(0.0f,0.0f,0.0f, 1f)
        mTriangle = Triangle()
        mSquare = Square()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0, width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        //绘制背景颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        mTriangle?.draw()
    }
}


class Triangle{
    private var mVertexBuffer:FloatBuffer? = null
    private var mTriangleCoords:FloatArray = floatArrayOf(0.0f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f)
    private var mColor:FloatArray = floatArrayOf(255f,0f,0f,1.0f)
    private var mVertexShaderCode:String = "attribute vec4 vPosition;" + "void main() {" + "  gl_Position = vPosition;" + "}"
    private var mFragmentShaderCode:String = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {" + "  gl_FragColor = vColor;" + "}"
    private var mProgram:Int? = null

    companion object{
        const val COORDS_PRE_VERTEX = 3

    }

    init{
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
        val byteBuf = ByteBuffer.allocateDirect(mTriangleCoords.size*4)
        // 数组排列用nativeOrder
        byteBuf.order(ByteOrder.nativeOrder())
        // 从ByteBuffer创建一个浮点缓冲区
        mVertexBuffer = byteBuf.asFloatBuffer()
        // 将坐标添加到FloatBuffer
        mVertexBuffer?.put(mTriangleCoords)
        // 设置缓冲区来读取第一个坐标
        mVertexBuffer?.position(0)
        //着色器
        val vertexShader = DrawGlRender.loadShader(GLES20.GL_VERTEX_SHADER,mVertexShaderCode)
        val fragmentShader = DrawGlRender.loadShader(GLES20.GL_FRAGMENT_SHADER,mFragmentShaderCode)

        // 创建空的OpenGL ES程序
        mProgram = GLES20.glCreateProgram();

        // 添加顶点着色器到程序中
        GLES20.glAttachShader(mProgram!!, vertexShader);

        // 添加片段着色器到程序中
        GLES20.glAttachShader(mProgram!!, fragmentShader);

        // 创建OpenGL ES程序可执行文件
        GLES20.glLinkProgram(mProgram!!);
    }

    private var mPositionHandle = 0
    private var mColorHandle = 0

    private val vertexCount: Int = mTriangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex


    fun draw() {
        // 将程序添加到OpenGL ES环境
        GLES20.glUseProgram(mProgram!!)

        // 获取顶点着色器的位置的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram!!, "vPosition")

        // 启用三角形顶点位置的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        //准备三角形坐标数据
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, mVertexBuffer
        )

        // 获取片段着色器的颜色的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram!!, "vColor")

        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0)

        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        // 禁用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}

class Square {
    private val vertexBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3
        var squareCoords = floatArrayOf(
            -0.5f, 0.5f, 0.0f,  // top left
            -0.5f, -0.5f, 0.0f,  // bottom left
            0.5f, -0.5f, 0.0f,  // bottom right
            0.5f, 0.5f, 0.0f
        ) // top right
    }

    init {
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
        val bb = ByteBuffer.allocateDirect(squareCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(squareCoords)
        vertexBuffer.position(0)

        // 初始化ByteBuffer，长度为arr数组的长度*2，因为一个short占2个字节
        val dlb = ByteBuffer.allocateDirect(drawOrder.size * 2)
        dlb.order(ByteOrder.nativeOrder())
        drawListBuffer = dlb.asShortBuffer()
        drawListBuffer.put(drawOrder)
        drawListBuffer.position(0)
    }
}

class BufferUtils{
    companion object{
        private fun intBufferUtil(arr: IntArray): IntBuffer? {
            val mBuffer: IntBuffer
            // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
            val qbb = ByteBuffer.allocateDirect(arr.size * 4)
            // 数组排列用nativeOrder
            qbb.order(ByteOrder.nativeOrder())
            mBuffer = qbb.asIntBuffer()
            mBuffer.put(arr)
            mBuffer.position(0)
            return mBuffer
        }

        private fun floatBufferUtil(arr: FloatArray): FloatBuffer? {
            val mBuffer: FloatBuffer
            // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
            val qbb = ByteBuffer.allocateDirect(arr.size * 4)
            // 数组排列用nativeOrder
            qbb.order(ByteOrder.nativeOrder())
            mBuffer = qbb.asFloatBuffer()
            mBuffer.put(arr)
            mBuffer.position(0)
            return mBuffer
        }

        private fun shortBufferUtil(arr: ShortArray): ShortBuffer? {
            val mBuffer: ShortBuffer
            // 初始化ByteBuffer，长度为arr数组的长度*2，因为一个short占2个字节
            val dlb = ByteBuffer.allocateDirect( // (# of coordinate values * 2 bytes per short)
                arr.size * 2
            )
            dlb.order(ByteOrder.nativeOrder())
            mBuffer = dlb.asShortBuffer()
            mBuffer.put(arr)
            mBuffer.position(0)
            return mBuffer
        }

    }
}