package com.jube.KotlinDemo

import android.util.Log
import com.google.common.util.concurrent.*
import kotlinx.coroutines.delay
import java.lang.Thread.sleep
import java.util.concurrent.*

class FutureDemo {
    companion object{

        /**
         * 虽然Future已经相关方法提供了异步编程的能力，但是获取结果十分不方便，只能通过阻塞或者轮询的方式获取结果，阻塞的方式显然与我们异步编程的初衷相违背,
         * 而且轮询的方式也很消耗的CPU资源，计算结果也不能及时拿到。面对这种情况，为什么不采用一种类似观察者模式的方式，当结果结算完成后实时通知到监听任务呢
         */
        fun FutureCallableDemo(){
            val TAG = "FutureCallableDemo"

            val executor:ExecutorService = Executors.newSingleThreadExecutor()
            val future: Future<Int> = executor.submit(MyCallable(3,10))
            //get方法会阻塞到获取结果
            Log.i(TAG,"before,time${System.currentTimeMillis()}")
            Log.i(TAG,"FutureCallable -> result:${future.get()},time:${System.currentTimeMillis()}")
            Log.i(TAG,"after,time:${System.currentTimeMillis()}")
            executor.shutdown()
            Log.i(TAG,"main end")
        }

        fun ListenableFutureDemo(){
            val TAG = "ListenableFutureDemo"
            //创建一个由invoke线程执行的线程池
            val executorService: ListeningExecutorService = MoreExecutors.newDirectExecutorService()
            //装饰自定义线程池的返回
            val executorService1:ListeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool())
            Log.i(TAG,"listenableFuture before,time:${System.currentTimeMillis()/1000}")
            //创建ListenableFuture
            val listenableFuture:ListenableFuture<Int> = executorService.submit(MyCallable(3,10))
            Log.i(TAG,"addListener before,time:${System.currentTimeMillis()/1000}")
            //添加监听,给异步任务添加监听,但不支持获取返回值,当计算完成时执行以下代码
            listenableFuture.addListener(Runnable {
                Log.i(TAG,"ListenableFuture -> listen succeed,time:${System.currentTimeMillis()/1000}")
            },executorService)
            Log.i(TAG,"after:time:${System.currentTimeMillis()/1000}")
            //添加回调，addListener方法不支持获取返回值，如果需要获取返回值，可以使用Futures.addCallBack静态方法，该类是对JDK Future的拓展
            Futures.addCallback(listenableFuture,object :FutureCallback<Any>{
                override fun onSuccess(result: Any?) {
                    Log.i(TAG,"Futures.addCallback -> result:${result},time:${System.currentTimeMillis()/1000}")
                }

                override fun onFailure(t: Throwable) {
                }
            }, executorService)
        }

        //合并多个Future
        fun MultiListenableFutureDemo(){

        }
    }
}

class MyCallable(a:Int,b:Int): Callable<Int>{
    private var mA: Int = a
    private var mB: Int = b

    override fun call(): Int {
        Log.i("ListenableFutureDemo","call start:${System.currentTimeMillis()/1000}")
        sleep(3000L)
        Log.i("ListenableFutureDemo","call end:${System.currentTimeMillis()/1000}")
        return mA*mB
    }
}

