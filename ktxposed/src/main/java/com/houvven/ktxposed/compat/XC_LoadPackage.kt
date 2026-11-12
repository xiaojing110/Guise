package com.houvven.ktxposed.compat

import android.content.pm.ApplicationInfo

/**
 * 旧 XC_LoadPackage 参数模型，内部由 API 102 的 PackageLoadedParam 构造。
 */
class XC_LoadPackage private constructor() {

    data class LoadPackageParam(
        val packageName: String,
        val processName: String,
        val classLoader: ClassLoader,
        val appInfo: ApplicationInfo,
        val isFirstApplication: Boolean
    )
}
