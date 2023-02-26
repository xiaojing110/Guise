package com.houvven.guise.xposed

import com.houvven.guise.BuildConfig
import com.houvven.guise.xposed.hook.BuildConfigHook
import com.houvven.guise.xposed.hook.OsBuildHook
import com.houvven.ktxposed.service.XposedHookLoadPackage

class MainHook : XposedHookLoadPackage {

    override val modulePackageName = BuildConfig.APPLICATION_ID

    override val hooks by lazy {
        listOf(
            BuildConfigHook,
            OsBuildHook
        )
    }
}