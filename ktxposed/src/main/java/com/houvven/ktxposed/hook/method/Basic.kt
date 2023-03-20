@file:Suppress("unused")

package com.houvven.ktxposed.hook.method

import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.ktxposed.util.toClasses
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Member
import java.lang.reflect.Method
import kotlin.reflect.KClass


@Throws(IllegalArgumentException::class)
private fun generateCallback(methodHook: MethodHook) = methodHook.run {
    if (replaceMethod == null && beforeHook == null && afterHook == null)
        throw IllegalArgumentException("MethodHook must have at least one callback")
    else if (replaceMethod != null && (beforeHook != null || afterHook != null))
        throw IllegalArgumentException("MethodHook can't have both replace and before/after callbacks")
    else if (replaceMethod == null) object : XC_MethodHook(priority) {
        override fun beforeHookedMethod(param: MethodHookParam) {
            beforeHook?.let { it(param) }
        }

        override fun afterHookedMethod(param: MethodHookParam) {
            afterHook?.let { it(param) }
        }
    }
    else object : XC_MethodReplacement(priority) {
        override fun replaceHookedMethod(param: MethodHookParam): Any? = replaceMethod!!(param)
    }
}


fun Member.hook(callback: MethodHook.Builder.() -> Unit) =
    MethodHook.Builder().apply(callback).build().run {
        runXposedCatching {
            generateCallback(this).let { XposedBridge.hookMethod(this@hook, it) }
        }
    }

fun Method.hook(callback: MethodHook.Builder.() -> Unit) =
    MethodHook.Builder().apply(callback).build().run {
        (this@hook as Member).hook(callback)
    }


fun Class<*>.hookAllMethods(methodName: String, callback: MethodHook.Builder.() -> Unit) =
    MethodHook.Builder().apply(callback).build().run {
        runXposedCatching {
            val unhooks: MutableSet<XC_MethodHook.Unhook> = HashSet()
            for (method in this@hookAllMethods.declaredMethods)
                if (method.name == methodName)
                    method.hook(callback).getOrNull()?.let { unhooks.add(it) }
            unhooks
        }
    }


fun KClass<*>.hookAllMethods(methodName: String, callback: MethodHook.Builder.() -> Unit) =
    java.hookAllMethods(methodName, callback)


fun Class<*>.hookAllMethods(callback: MethodHook.Builder.() -> Unit) =
    MethodHook.Builder().apply(callback).build().run {
        runXposedCatching {
            val unhooks: MutableSet<XC_MethodHook.Unhook> = HashSet()
            for (method in this@hookAllMethods.declaredMethods)
                method.hook(callback).getOrNull()?.let { unhooks.add(it) }
            unhooks
        }
    }


fun KClass<*>.hookAllMethods(callback: MethodHook.Builder.() -> Unit) =
    java.hookAllMethods(callback)


fun Class<*>.hookAllConstructors(callback: MethodHook.Builder.() -> Unit) =
    MethodHook.Builder().apply(callback).build().run {
        runXposedCatching {
            val unhooks: MutableSet<XC_MethodHook.Unhook> = java.util.HashSet()
            for (constructor in this@hookAllConstructors.declaredConstructors)
                unhooks.add(XposedBridge.hookMethod(constructor, generateCallback(this)))
            unhooks
        }
    }


fun KClass<*>.hookAllConstructors(callback: MethodHook.Builder.() -> Unit) =
    java.hookAllConstructors(callback)


fun Class<*>.hookMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    callback: MethodHook.Builder.() -> Unit
): Result<XC_MethodHook.Unhook> {
    return runXposedCatching {
        findMethodExact(methodName, *parameterTypes).hook(callback).getOrThrow()
    }
}


fun KClass<*>.hookMethod(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    callback: MethodHook.Builder.() -> Unit
) = java.hookMethod(methodName, *parameterTypes.toClasses(), callback = callback)


fun Class<*>.hookConstructor(
    vararg parameterTypes: Class<*>,
    callback: MethodHook.Builder.() -> Unit
): Result<XC_MethodHook.Unhook> {
    return runXposedCatching {
        findConstructorExact(*parameterTypes).hook(callback).getOrThrow()
    }
}

fun KClass<*>.hookConstructor(
    vararg parameterTypes: KClass<*>,
    callback: MethodHook.Builder.() -> Unit
) = java.hookConstructor(*parameterTypes.toClasses(), callback = callback)