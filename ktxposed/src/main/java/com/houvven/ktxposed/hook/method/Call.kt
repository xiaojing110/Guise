@file:Suppress("unused")

package com.houvven.ktxposed.hook.method

import com.houvven.ktxposed.util.runXposedCatching
import de.robv.android.xposed.XposedHelpers
import kotlin.reflect.KClass


fun Class<*>.callStaticMethod(
    methodName: String, vararg args: Any?
) = runXposedCatching { XposedHelpers.callStaticMethod(this, methodName, *args) }


fun Class<*>.callStaticMethod(
    methodName: String, parameterTypes: Array<Class<*>>, vararg args: Any?
) = runXposedCatching { XposedHelpers.callStaticMethod(this, methodName, parameterTypes, *args) }


fun KClass<*>.callStaticMethod(methodName: String, vararg args: Any?) =
    java.callStaticMethod(methodName, *args)


fun KClass<*>.callStaticMethod(
    methodName: String,
    parameterTypes: Array<KClass<*>>,
    vararg args: Any?
) = java.callStaticMethod(methodName, parameterTypes, *args)


fun Any.callMethod(
    methodName: String, vararg args: Any?
) = runXposedCatching { XposedHelpers.callMethod(this, methodName, *args) }


fun Any.callMethod(
    methodName: String, parameterTypes: Array<Class<*>>, vararg args: Any?
) = runXposedCatching { XposedHelpers.callMethod(this, methodName, parameterTypes, *args) }