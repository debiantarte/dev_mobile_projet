package com.example.dm_project.network

import android.app.Application

class App : Application()
{
    override fun onCreate() {
        super.onCreate()
        API.INSTANCE = API(this)
    }
}
