@file:Suppress("unused")

package com.houvven.ktxposed.hook.method

import java.lang.reflect.Method
import kotlin.reflect.KClass

fun Method.afterSetResult(result: () -> Any?) = afterHook { it.result = result() }
fun Method.replaceResult(result: () -> Any?) = replaceHook { return@replaceHook result() }
fun Method.replaceToUnit() = replaceResult { }

fun Class<*>.afterSetMethodResult(
    methodName: String,
    vararg parameterTypes: Class<*>,
    result: () -> Any?
) = afterHookMethod(methodName, *parameterTypes) { it.result = result() }

fun Class<*>.replaceMethodResult(
    methodName: String,
    vararg parameterTypes: Class<*>,
    result: () -> Any?
) = replaceHookMethod(methodName, *parameterTypes) { return@replaceHookMethod result() }

fun Class<*>.replaceMethodToUnit(
    methodName: String,
    vararg parameterTypes: Class<*>
) = replaceMethodResult(methodName, *parameterTypes) { }

fun KClass<*>.afterSetMethodResult(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    result: () -> Any?
) = afterHookMethod(methodName, *parameterTypes) { it.result = result() }

fun KClass<*>.replaceMethodResult(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    result: () -> Any?
) = replaceHookMethod(methodName, *parameterTypes) { return@replaceHookMethod result() }

fun KClass<*>.replaceMethodToUnit(
    methodName: String,
    vararg parameterTypes: KClass<*>
) = replaceMethodResult(methodName, *parameterTypes) {}

fun Class<*>.afterSetAllMethodsResult(methodName: String, result: () -> Any?) =
    afterHookAllMethods(methodName) { it.result = result() }

fun Class<*>.replaceAllMethodsResult(methodName: String, result: () -> Any?) =
    replaceHookAllMethods(methodName) { return@replaceHookAllMethods result() }

fun KClass<*>.afterSetAllMethodsResult(methodName: String, result: () -> Any?) =
    afterHookAllMethods(methodName) { it.result = result() }

fun KClass<*>.replaceAllMethodsResult(methodName: String, result: () -> Any?) =
    replaceHookAllMethods(methodName) { return@replaceHookAllMethods result() }


infix fun Method.afterSetResult(result: Any?) = afterHook { it.result = result }
infix fun Method.replaceResult(result: Any?) = replaceHook { return@replaceHook result }
