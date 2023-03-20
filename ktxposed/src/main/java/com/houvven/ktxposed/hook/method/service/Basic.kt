package com.houvven.ktxposed.hook.method.service

import com.houvven.ktxposed.LocalHookParam
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage


interface Basic : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        LocalHookParam.lpparam = lpparam
    }
}