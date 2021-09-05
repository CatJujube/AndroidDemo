//
// Created by tsinghualei on 2021/9/5.
//

#include <jni.h>

/**
 * @param env:原生代码通过JNIEnv接口指针提供的各种函数来使用虚拟机功能，JNIEnv是一个指向线程局部数据的指针，线程局部数据中包含指向函数表的指针
 **/
extern "C" jstring Java_com_jube_NdkDemo_HelloJni_hello(JNIEnv *env, jobject thiz)
{
//    return (*env).NewStringUTF("Hello from JNI !  Compiled with ABI ");
    return env->NewStringUTF("JNI Hello World!");
}