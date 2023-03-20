package com.houvven.twig

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

class ContextAmbient : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var current: Context
    }

    override fun onCreate() {
        super.onCreate()
        current = applicationContext
        MMKV.initialize(this)
    }
}