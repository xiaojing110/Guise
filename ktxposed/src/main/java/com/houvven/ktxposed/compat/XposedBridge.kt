package com.houvven.ktxposed.compat

import android.content.SharedPreferences
import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Executable
import java.lang.reflect.Member

/**
 * 旧 XposedBridge 的最小兼容实现，底层使用 LSPosed API 102。
 */
object XposedBridge {

    @Volatile
    private var api: XposedInterface? = null

    val BOOTCLASSLOADER: ClassLoader =
        ClassLoader.getSystemClassLoader().parent ?: ClassLoader.getSystemClassLoader()

    fun attach(xposed: XposedInterface) {
        api = xposed
    }

    fun remotePreferences(name: String): SharedPreferences? = runCatching {
        api?.getRemotePreferences(name)
    }.getOrNull()

    @JvmStatic
    fun hookMethod(hookMethod: Member, callback: XC_MethodHook): XC_MethodHook.Unhook {
        val xposed = api ?: throw IllegalStateException("LSPosed API 102 is not attached")
        val executable = hookMethod as? Executable
            ?: throw IllegalArgumentException("$hookMethod is not a method or constructor")

        val handle = xposed.hook(executable)
            .setPriority(callback.priority)
            .intercept(object : XposedInterface.Hooker {
                override fun intercept(chain: XposedInterface.Chain): Any? =
                    callback.handle(chain)
            })

        return XC_MethodHook.Unhook(handle)
    }
}
