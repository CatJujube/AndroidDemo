package com.jube.androiddemo.PhotoGallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"
class FlickrFetchr {
    private val flickrApi:FlickrApi

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://www.flickr.com/services/rest/?format=json&jsoncallback=1&extras=url_s").addConverterFactory(
            ScalarsConverterFactory.create()).build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchContents():LiveData<String>{
        val responseLiveData:MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<String> = flickrApi.fetchContents()
        flickrRequest.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()
                Log.d(PhotoGalleryFragment.TAG,"Response received:${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(PhotoGalleryFragment.TAG,"Fail to Fetch photos",t)
            }
        })
        return responseLiveData
    }
}