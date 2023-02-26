package com.houvven.ktxposed.service

import com.houvven.ktxposed.LocalHookParam
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

interface HookLoadPackageBase : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        LocalHookParam.lpparam = lpparam
    }
}