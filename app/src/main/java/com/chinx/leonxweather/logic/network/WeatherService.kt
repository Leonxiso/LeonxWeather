package com.chinx.leonxweather.logic.network

import com.chinx.leonxweather.LeonxWeatherApplication
import com.chinx.leonxweather.logic.model.DailyResponse
import com.chinx.leonxweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    //获取实时天气信息
    @GET("v2.5/${LeonxWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    // 获取未来天气信息
    @GET("v2.5/${LeonxWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}