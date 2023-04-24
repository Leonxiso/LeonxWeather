package com.chinx.leonxweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.chinx.leonxweather.logic.dao.PlaceDao
import com.chinx.leonxweather.logic.model.Place
import com.chinx.leonxweather.logic.model.Weather
import com.chinx.leonxweather.logic.network.LeonxWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    // 封装try catch，仅进行一次处理
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }

    // 搜索全球地址数据
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = LeonxWeatherNetwork.searchPlaces(query)
        Log.i("PlaceResponse", placeResponse.toString())
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
        }
    }

    // 刷新天气数据信息
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
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
                        "Realtime response status is ${realtimeResponse.status}" + "Daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    // 本地保存数据
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    // 获得本地数据
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    // 是否保存
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}