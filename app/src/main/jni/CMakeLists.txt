cmake_minimum_required(VERSION 3.4.1)

add_library(
        helloJni # so 库的名称 libhello-jni.so
        SHARED # 设置为分享库
        # 指定C源文件的路径，指向公共cpp-src目录
        ./HelloJni.cpp
)

#find_library(
#
#)

# 链接hello-jni库依赖的库，注意下面变量名的配置
target_link_libraries(
        helloJni
)
