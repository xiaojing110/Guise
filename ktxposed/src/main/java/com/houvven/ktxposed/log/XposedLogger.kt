package com.houvven.ktxposed.log

import android.util.Log
import com.houvven.androidc.ktx.ifFalse
import com.houvven.ktxposed.LocalHookParam

@Suppress("unused")
object XposedLogger {

    private val logs = mutableListOf<String>()

    private const val TAG = "XposedLogger"

    fun d(tag: String, message: String) {
        Log.d(tag, message)
        // Log.e("tag", RuntimeException(""))
    }


    fun e(throwable: Throwable) {
        val stackTrace = throwable.stackTraceToString()
        Log.e(TAG, stackTrace)
    }

    private fun log() {
        runCatching {
            LocalHookParam.lpparam

        }.onFailure {
            val resolve = LocalHookParam.context.dataDir.resolve("guise.log")
            resolve.exists() ifFalse { resolve.createNewFile() }
            resolve.appendText(logs.joinToString("\n"))
        }.onFailure {

        }
    }
}