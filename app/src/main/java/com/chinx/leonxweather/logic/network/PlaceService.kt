package com.chinx.leonxweather.logic.network

import com.chinx.leonxweather.LeonxWeatherApplication
import com.chinx.leonxweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${LeonxWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}