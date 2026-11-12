package com.houvven.ktxposed.compat

import io.github.libxposed.api.XposedInterface

open class XC_MethodReplacement @JvmOverloads constructor(
    priority: Int = XCallback.PRIORITY_DEFAULT
) : XC_MethodHook(priority) {

    open fun replaceHookedMethod(param: MethodHookParam): Any? = null

    override fun handle(chain: XposedInterface.Chain): Any? =
        replaceHookedMethod(MethodHookParam(chain))
}
