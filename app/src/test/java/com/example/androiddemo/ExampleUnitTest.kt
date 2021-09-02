package com.example.androiddemo

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    companion object{
        const val TAG = "ExampleUnitTest_Log"
    }
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        print("hello world!")
        Log.i(TAG,"hello world")
    }
}