package com.chinx.leonxweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 可以在任何位置通过调用LeonxWeatherApplication.context来获取Context对象
class LeonxWeatherApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "5nQo47XxDqIQHyzx"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}