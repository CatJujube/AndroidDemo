package com.example.kotlindemo

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class CoroutineDemo {
    companion object{
        const val TAG = "CoroutineDemo"

        suspend fun runSuspend(){
            Log.i(TAG,"main start!")
            val value1 = GlobalScope.async{ doSomething1()}
            val value2 = GlobalScope.async { doSomething2() }
            Log.i(TAG,"main get result: ${value1.await()}+${value2.await()}")
            Log.i(TAG,"main end!")
        }

        private suspend fun doSomething1():Int{
            Log.i(TAG,"doSomething1 before delay!")
            delay(5000L)
            Log.i(TAG,"doSomething1 after delay!")
            return 11
        }

        private suspend fun doSomething2():Int{
            Log.i(TAG,"doSomething2 before delay!")
            delay(5000L)
            Log.i(TAG,"doSomething2 after delay!")
            return 22
        }
    }
}