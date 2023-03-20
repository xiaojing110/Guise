package com.houvven.ktxposed.util

import com.houvven.ktxposed.log.XposedLogger
import kotlin.reflect.KClass

inline fun <reified T> runXposedCatching(block: () -> T): Result<T> {
    val result = runCatching { block() }
    result.onFailure {
        XposedLogger.e(it)
    }
    return result
}


fun Array<out KClass<*>>.toClasses(): Array<Class<*>> = map { it.java }.toTypedArray()