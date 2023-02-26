@file:Suppress("unused")

package com.houvven.ktxposed.lpparam

import com.houvven.ktxposed.util.runXposedCatching
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method
import kotlin.reflect.KClass


/**
 * @see XposedBridge.hookMethod
 */
fun Method.hook(callback: XC_MethodHook): Result<XC_MethodHook.Unhook> =
    runXposedCatching {
        XposedBridge.hookMethod(this, callback)
    }


/**
 * @see XposedHelpers.findAndHookMethod
 */
fun Class<*>.findAndHookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    callback: XC_MethodHook
) = runXposedCatching {
    XposedHelpers.findAndHookMethod(this, methodName, *parameterTypes, callback)
}


/**
 * @see XposedHelpers.findAndHookMethod
 */
fun KClass<*>.findAndHookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    callback: XC_MethodHook
) = this.java.findAndHookMethod(
    methodName,
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)


/**
 * @see XposedHelpers.findAndHookConstructor
 */
fun Class<*>.findAndHookConstructor(
    vararg parameterTypes: Class<*>,
    callback: XC_MethodHook
) = runXposedCatching {
    XposedHelpers.findAndHookConstructor(this, *parameterTypes, callback)
}


/**
 * @see XposedHelpers.findAndHookConstructor
 */
fun KClass<*>.findAndHookConstructor(
    vararg parameterTypes: KClass<*>,
    callback: XC_MethodHook
) = this.java.findAndHookConstructor(
    *parameterTypes.map { it.java }.toTypedArray(),
    callback = callback
)

/**
 * @see XposedBridge.hookAllMethods
 */
fun Class<*>.hookAllMethods(
    methodName: String,
    callback: XC_MethodHook
): MutableSet<XC_MethodHook.Unhook> = XposedBridge.hookAllMethods(this, methodName, callback)


/**
 * @see XposedBridge.hookAllMethods
 */
fun KClass<*>.hookAllMethods(
    methodName: String,
    callback: XC_MethodHook
) = this.java.hookAllMethods(methodName, callback)


/**
 * @see XposedBridge.hookAllConstructors
 */
fun Class<*>.hookAllConstructors(callback: XC_MethodHook): MutableSet<XC_MethodHook.Unhook> =
    XposedBridge.hookAllConstructors(this, callback)


/**
 * @see XposedBridge.hookAllConstructors
 */
fun KClass<*>.hookAllConstructors(callback: XC_MethodHook) = this.java.hookAllConstructors(callback)





