package com.houvven.ktxposed.hook.method.service

import com.houvven.ktxposed.compat.XC_LoadPackage

/**
 * 主 Hook 入口接口。
 *
 * LSPosed API 102 的入口由 MainHook 继承 XposedModule 完成，这里只保留
 * 按包分发 Hook 的职责。
 */
interface HookLoadPackageEntrance {

    val modulePackageName: String

    fun loadPackageHook(lpparam: XC_LoadPackage.LoadPackageParam)

    fun doHookLoadPackages(hooks: List<HookLoadPackage>) {
        hooks.forEach { it.onHook() }
    }
}
