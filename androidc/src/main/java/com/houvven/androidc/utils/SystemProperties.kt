package com.houvven.androidc.utils

import android.annotation.SuppressLint

@SuppressLint("PrivateApi")
object SystemProperties {

    private const val CLASS_NAME = "android.os.SystemProperties"

    private val clazz by lazy { Class.forName(CLASS_NAME) }

    fun get(key: String): String {
        return clazz.getMethod("get", String::class.java).invoke(null, key) as String
    }

    fun get(key: String, defaultValue: String): String {
        return clazz.getMethod("get", String::class.java, String::class.java)
            .invoke(null, key, defaultValue) as String
    }
}