package com.houvven.ktxposed.hook.method.service

import com.houvven.ktxposed.LocalHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage

interface HookLoadPackage : Basic {

    val lpparam: XC_LoadPackage.LoadPackageParam get() = LocalHookParam.lpparam

    fun onHook()
}