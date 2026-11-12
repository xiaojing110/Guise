package com.houvven.ktxposed.compat

import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Member

/**
 * 旧 Xposed API 的 Hook 回调模型，内部桥接到 LSPosed API 102。
 */
open class XC_MethodHook @JvmOverloads constructor(
    val priority: Int = XCallback.PRIORITY_DEFAULT
) {

    open fun beforeHookedMethod(param: MethodHookParam) = Unit

    open fun afterHookedMethod(param: MethodHookParam) = Unit

    internal open fun handle(chain: XposedInterface.Chain): Any? {
        val param = MethodHookParam(chain)
        beforeHookedMethod(param)
        if (param.returnEarly) return param.result

        try {
            val value = chain.proceed(param.args)
            param.result = value
        } catch (t: Throwable) {
            param.throwable = t
        }

        afterHookedMethod(param)
        param.throwable?.let { throw it }
        return param.result
    }

    class MethodHookParam internal constructor(
        private val chain: XposedInterface.Chain
    ) {
        val method: Member get() = chain.executable
        val thisObject: Any? get() = chain.thisObject

        var args: Array<Any?> = chain.args.toTypedArray()

        /**
         * 与旧 API 一致：在 before 回调里赋值 result 会跳过原方法。
         */
        var result: Any? = null
            set(value) {
                field = value
                returnEarly = true
            }

        var returnEarly: Boolean = false
        var throwable: Throwable? = null

        fun hasThrowable(): Boolean = throwable != null
    }

    class Unhook internal constructor(
        private val handle: XposedInterface.HookHandle
    ) {
        fun unhook() {
            handle.unhook()
        }
    }
}
