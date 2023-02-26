package com.houvven.androidc.util

import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("unused", "MemberVisibilityCanBePrivate")
object TimeFormat {
    const val FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS"
    const val FORMAT_DATE = "yyyy-MM-dd"
    const val FORMAT_TIME = "HH:mm:ss.SSS"
    const val FORMAT_TIME_NO_MILLIS = "HH:mm:ss"


    fun format(time: Long, format: String = FORMAT_FULL): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(time)
    }
}