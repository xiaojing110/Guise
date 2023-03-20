package com.houvven.ktxposed

import android.annotation.SuppressLint
import android.app.AndroidAppHelper
import android.content.Context
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

@Suppress("unused")
object LocalHookParam {
    /**
     * @see XC_LoadPackage.LoadPackageParam
     */
    lateinit var lpparam: XC_LoadPackage.LoadPackageParam
        internal set

    /**
     * @see Context
     * @see AndroidAppHelper.currentApplication
     */
    @get:SuppressLint("StaticFieldLeak")
    val context: Context by lazy { AndroidAppHelper.currentApplication().applicationContext }

    val BOOT_CLASS_LOADER: ClassLoader get() = XposedBridge.BOOTCLASSLOADER
}