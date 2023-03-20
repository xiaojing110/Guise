package com.houvven.ktxposed.hook.method.service

import com.houvven.ktxposed.HookStatus
import com.houvven.ktxposed.hook.method.afterSetMethodResult
import com.houvven.ktxposed.hook.method.findClass
import com.houvven.ktxposed.log.XposedLogger
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * 主Hook入口接口
 * @author Houvven
 */
interface HookLoadPackageEntrance : Basic {

    val modulePackageName: String

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        super.handleLoadPackage(lpparam)
        if (lpparam.packageName == modulePackageName) {
            doActivateModule()
        } else {
            loadPackageHook(lpparam)
        }
    }

    fun loadPackageHook(lpparam: XC_LoadPackage.LoadPackageParam)

    private fun doActivateModule() {
        findClass(HookStatus.Companion::class.java.name) {
            XposedLogger.d("HookStatus isHooked: ${HookStatus.isHooked}")
            afterSetMethodResult("isHooked") { true }
        }
    }

    fun doHookLoadPackages(hooks: List<HookLoadPackage>) {
        hooks.forEach {
            it.onHook()
        }
    }
}