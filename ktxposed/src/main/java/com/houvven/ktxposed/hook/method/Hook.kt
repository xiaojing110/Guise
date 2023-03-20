@file:Suppress("unused")

package com.houvven.ktxposed.hook.method


import com.houvven.ktxposed.util.toClasses
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Method
import kotlin.reflect.KClass


inline fun Method.beforeHook(
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hook { beforeHook { callback(it) } }


inline fun Method.afterHook(
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hook { afterHook { callback(it) } }


inline fun Method.replaceHook(
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = hook{ replaceMethod { callback(it) } }


inline fun Class<*>.beforeHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookMethod(methodName, *parameterTypes) { beforeHook { callback(it) } }


inline fun KClass<*>.beforeHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.beforeHookMethod(
    methodName,
    *parameterTypes.toClasses(),
    callback = callback
)


inline fun Class<*>.afterHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookMethod(methodName, *parameterTypes) { afterHook { callback(it) } }


inline fun KClass<*>.afterHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.afterHookMethod(
    methodName,
    *parameterTypes.toClasses(),
    callback = callback
)


inline fun Class<*>.replaceHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = hookMethod(methodName, *parameterTypes) { replaceMethod { callback(it) } }


inline fun KClass<*>.replaceHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = this.java.replaceHookMethod(
    methodName,
    *parameterTypes.toClasses(),
    callback = callback
)

inline fun Class<*>.beforeHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookConstructor(*parameterTypes) { beforeHook { callback(it) } }


inline fun KClass<*>.beforeHookConstructor(
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.beforeHookConstructor(
    *parameterTypes.toClasses(),
    callback = callback
)


inline fun Class<*>.afterHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookConstructor(*parameterTypes) { afterHook { callback(it) } }


inline fun KClass<*>.afterHookConstructor(
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.afterHookConstructor(
    *parameterTypes.toClasses(),
    callback = callback
)


inline fun Class<*>.replaceHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = hookConstructor(*parameterTypes) { replaceMethod { callback(it) } }


inline fun KClass<*>.replaceHookConstructor(
    vararg parameterTypes: KClass<*>,
    crossinline callback: (XC_MethodHook.MethodHookParam) -> Any?
) = this.java.replaceHookConstructor(
    *parameterTypes.toClasses(),
    callback = callback
)

fun Class<*>.beforeHookAllMethods(
    methodName: String,
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookAllMethods(methodName) { beforeHook { callback(it) } }


fun KClass<*>.beforeHookAllMethods(
    methodName: String,
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.beforeHookAllMethods(methodName, callback)


fun Class<*>.afterHookAllMethods(
    methodName: String,
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookAllMethods(methodName) { afterHook { callback(it) } }

fun KClass<*>.afterHookAllMethods(
    methodName: String,
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.afterHookAllMethods(methodName, callback)

fun Class<*>.replaceHookAllMethods(
    methodName: String,
    callback: (XC_MethodHook.MethodHookParam) -> Any?
) = hookAllMethods(methodName) { replaceMethod { callback(it) } }

fun KClass<*>.replaceHookAllMethods(
    methodName: String,
    callback: (XC_MethodHook.MethodHookParam) -> Any?
) = this.java.replaceHookAllMethods(methodName, callback)

fun Class<*>.beforeHookAllConstructors(
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookAllConstructors { beforeHook { callback(it) } }

fun KClass<*>.beforeHookAllConstructors(
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.beforeHookAllConstructors(callback)

fun Class<*>.afterHookAllConstructors(
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = hookAllConstructors { afterHook { callback(it) } }

fun KClass<*>.afterHookAllConstructors(
    callback: (XC_MethodHook.MethodHookParam) -> Unit
) = this.java.afterHookAllConstructors(callback)

fun Class<*>.replaceHookAllConstructors(
    callback: (XC_MethodHook.MethodHookParam) -> Any?
) = hookAllConstructors { replaceMethod { callback(it) } }

fun KClass<*>.replaceHookAllConstructors(
    callback: (XC_MethodHook.MethodHookParam) -> Any?
) = this.java.replaceHookAllConstructors(callback)


fun Class<*>.beforeHookAllMethodsIfExits() {

}

