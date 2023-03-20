package com.houvven.ktxposed.log

import android.util.Log

@Suppress("unused")
object XposedLogger {

    var isDebug = true

    private val logs = mutableListOf<String>()

    private const val TAG = "XposedLogger"

    fun load() {
        TODO()
    }

    fun i(message: String) {
        log(message)
    }

    fun d(message: String) {
        if (!isDebug) return
        Log.d(TAG, message)
    }

    fun w(message: String) {
        Log.w(TAG, message)
        log(message)
    }

    fun e(string: String) {
        Log.e(TAG, string)
        log(string)
    }

    fun e(throwable: Throwable) {
        val stackTrace = throwable.stackTraceToString()
        Log.e(TAG, stackTrace)
        log(stackTrace)
    }

    private fun log(message: String) {
        logs.add(message)
        
    }
}