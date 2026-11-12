package com.houvven.ktxposed.hook.method.service

import com.houvven.ktxposed.LocalHookParam
import com.houvven.ktxposed.compat.XC_LoadPackage

interface HookLoadPackage {

    val lpparam: XC_LoadPackage.LoadPackageParam get() = LocalHookParam.lpparam

    fun onHook()
}
