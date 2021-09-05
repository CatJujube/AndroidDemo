package com.jube.androiddemo.PhotoGallery

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {
    @GET("/")
    fun fetchContents():Call<String>
}