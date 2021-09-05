package com.jube.NdkDemo;

public class HelloJni {
    //Java提供的两个静态方法，用于运行时加载原生库
    static {
        System.loadLibrary("helloJni");
    }

    // 声明 Native 方法
    public native String hello();
}
