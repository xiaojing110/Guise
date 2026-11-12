package com.houvven.ktxposed

import android.content.Context
import com.houvven.ktxposed.compat.XC_LoadPackage
import com.houvven.ktxposed.compat.XposedBridge

@Suppress("unused")
object LocalHookParam {

    /**
     * @see XC_LoadPackage.LoadPackageParam
     */
    lateinit var lpparam: XC_LoadPackage.LoadPackageParam

    /**
     * 目标进程的 Application Context。
     *
     * API 102 不再提供 android.app.AndroidAppHelper，这里通过
     * ActivityThread.currentApplication() 获取。
     */
    val context: Context by lazy {
        val app = Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication")
            .invoke(null)
        app as Context
    }

    val BOOT_CLASS_LOADER: ClassLoader get() = XposedBridge.BOOTCLASSLOADER
}
