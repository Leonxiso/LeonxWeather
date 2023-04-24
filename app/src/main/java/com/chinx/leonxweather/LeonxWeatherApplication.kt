package com.chinx.leonxweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 可以在任何位置通过调用LeonxWeatherApplication.context来获取Context对象
class LeonxWeatherApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "nAG9OssfhDwniHQ2" // chin_lg7@qq.com → "5nQo47XxDqIQHyzx"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}