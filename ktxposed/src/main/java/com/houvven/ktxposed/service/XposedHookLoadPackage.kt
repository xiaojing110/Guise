package com.houvven.ktxposed.service

import com.houvven.ktxposed.HookStatus
import com.houvven.ktxposed.lpparam.beforeSetResult
import com.houvven.ktxposed.lpparam.findClass
import com.houvven.ktxposed.lpparam.findMethodExact
import com.houvven.ktxposed.lpparam.setStaticField
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

interface XposedHookLoadPackage : HookLoadPackageBase {

    val modulePackageName: String

    val hooks: List<HookLoadPackage>

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        super.handleLoadPackage(lpparam)
        if (lpparam.packageName == modulePackageName) {
            doActivateModule()
        } else {
            loadPackageHooks()
        }
    }

    private fun loadPackageHooks() {
        hooks.forEach { it.onHook() }
    }

    private fun doActivateModule() {
        findClass(HookStatus.Companion::class.java.name)
            .findMethodExact("isHooked")
            .beforeSetResult(true)
    }
}