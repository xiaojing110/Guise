package com.houvven.ktxposed.hook.method

import com.houvven.ktxposed.log.XposedLogger
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XCallback

@Suppress("unused")
data class MethodHook(
    var priority: Int = XCallback.PRIORITY_DEFAULT,
    var beforeHook: ((XC_MethodHook.MethodHookParam) -> Unit)? = null,
    var afterHook: ((XC_MethodHook.MethodHookParam) -> Unit)? = null,
    var replaceMethod: ((XC_MethodHook.MethodHookParam) -> Any?)? = null
) {
    class Builder {
        private val methodHook = MethodHook()

        fun priority(prioritize: () -> Int) {
            this.methodHook.priority = prioritize()
        }

        fun beforeHook(callback: (XC_MethodHook.MethodHookParam) -> Unit) {
            this.methodHook.beforeHook = {
                // XposedLogger.d("beforeHookedMethod: ${it.method}")
                callback(it)
            }
        }

        fun afterHook(callback: (XC_MethodHook.MethodHookParam) -> Unit) {
            this.methodHook.afterHook = {
                // XposedLogger.d("afterHookedMethod: ${it.method}")
                callback(it)
            }
        }


        fun replaceMethod(callback: (XC_MethodHook.MethodHookParam) -> Any?) {
            this.methodHook.replaceMethod = {
                // XposedLogger.d("replaceMethod: ${it.method}")
                callback(it)
            }
        }


        internal fun build() = methodHook
    }
}
