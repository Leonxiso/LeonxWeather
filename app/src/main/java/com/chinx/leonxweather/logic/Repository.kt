package com.chinx.leonxweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.chinx.leonxweather.logic.model.Place
import com.chinx.leonxweather.logic.network.LeonxWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = LeonxWeatherNetwork.searchPlaces(query)
            Log.i("Response", placeResponse.toString())
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
}