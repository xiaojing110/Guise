@file:Suppress("unused")


package com.houvven.ktxposed.lpparam

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import java.lang.reflect.Method
import kotlin.reflect.KClass


inline fun Method.beforeHook(
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hook(object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
        callback(param)
    }
})


inline fun Method.afterHook(
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hook(object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
        callback(param)
    }
})


inline fun Method.replaceHook(
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = hook(object : XC_MethodReplacement() {
    override fun replaceHookedMethod(param: MethodHookParam): Any? {
        return callback(param)
    }
})


inline fun Class<*>.beforeHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = findAndHookMethod(methodName, *parameterTypes, callback = object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
        callback(param)
    }
})


inline fun KClass<*>.beforeHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.beforeHookMethod(
    methodName,
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


inline fun Class<*>.afterHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = findAndHookMethod(methodName, *parameterTypes, callback = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
        callback(param)
    }
})


inline fun KClass<*>.afterHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.afterHookMethod(
    methodName,
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


inline fun Class<*>.replaceHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = findAndHookMethod(
    methodName,
    *parameterTypes,
    callback = object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam): Any? {
            return callback(param)
        }
    })


inline fun KClass<*>.replaceHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = this.java.replaceHookMethod(
    methodName,
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


inline fun Class<*>.beforeHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = findAndHookConstructor(*parameterTypes, callback = object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
        callback(param)
    }
})


inline fun KClass<*>.beforeHookConstructor(
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.beforeHookConstructor(
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


inline fun Class<*>.afterHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = findAndHookConstructor(*parameterTypes, callback = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
        callback(param)
    }
})


inline fun KClass<*>.afterHookConstructor(
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.afterHookConstructor(
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


inline fun Class<*>.replaceHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = findAndHookConstructor(*parameterTypes, callback = object : XC_MethodReplacement() {
    override fun replaceHookedMethod(param: MethodHookParam): Any? {
        return callback(param)
    }
})


inline fun KClass<*>.replaceHookConstructor(
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = this.java.replaceHookConstructor(
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


fun Method.beforeSetResult(result: Any?) = beforeHook { it.result = result }


fun Method.afterSetResult(result: Any?) = beforeHook { it.result = result }



