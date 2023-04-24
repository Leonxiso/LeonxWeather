package com.chinx.leonxweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.chinx.leonxweather.logic.model.Place
import com.chinx.leonxweather.logic.model.Weather
import com.chinx.leonxweather.logic.network.LeonxWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object RepositoryBak {
    // 搜索全球地址数据
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = LeonxWeatherNetwork.searchPlaces(query)
            Log.i("PlaceResponse", placeResponse.toString())
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

    // 刷新天气数据信息
    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    LeonxWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    LeonxWeatherNetwork.getDailyWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                Log.i("RealtimeResponse", realtimeResponse.toString())
                Log.i("DailyResponse", dailyResponse.toString())
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "Realtime response status is ${realtimeResponse.status}" +
                            "Daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure<Weather>(e)
        }
        emit(result)
    }
}