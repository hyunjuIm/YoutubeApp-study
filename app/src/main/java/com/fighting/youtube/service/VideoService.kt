package com.fighting.youtube.service

import com.fighting.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/10793bf9-a2f9-4db5-882a-6cfa40dd3840")
    fun listVideos(): Call<VideoDto>
}