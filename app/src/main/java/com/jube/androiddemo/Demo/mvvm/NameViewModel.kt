package com.jube.androiddemo.Demo.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * step1 : 创建 LiveData 对象
 * LiveData 是一种可用于任何数据的封装容器，其中包括可实现 Collections 的对象，如 List。LiveData 对象通常存储在 ViewModel 对象中，并可通过 getter 方法进行访问，如以下示例中所示：
 **/
class NameViewModel: ViewModel() {
    val currentName:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}