package com.houvven.ktxposed.util

import com.houvven.ktxposed.log.XposedLogger

inline fun <reified T> runXposedCatching(block: () -> T): Result<T> {
    val result = runCatching { block() }
    result.onFailure { XposedLogger.e(it) }
    return result
}