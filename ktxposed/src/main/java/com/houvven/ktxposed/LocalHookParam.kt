package com.houvven.ktxposed

import android.app.AndroidAppHelper
import android.content.Context
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage


object LocalHookParam {
    /**
     * @see XC_LoadPackage.LoadPackageParam
     */
    lateinit var lpparam: XC_LoadPackage.LoadPackageParam
        internal set

    /**
     * @see XC_InitPackageResources.InitPackageResourcesParam
     */
    lateinit var resparam: XC_InitPackageResources.InitPackageResourcesParam
        internal set

    /**
     * @see Context
     * @see AndroidAppHelper.currentApplication
     */
    val context: Context by lazy { AndroidAppHelper.currentApplication() }
}