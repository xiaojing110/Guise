package com.houvven.guise.xposed.hook

import com.houvven.guise.xposed.LoadPackageHandler
import com.houvven.ktx_xposed.hook.findClassIfExists
import com.houvven.ktx_xposed.hook.lppram
import com.houvven.ktx_xposed.hook.setStaticField

class BuildConfigHook : LoadPackageHandler {

    override fun onHook() {
        val name = lppram.packageName
        val targetClass = findClassIfExists("$name.BuildConfig") ?: return

        if (config.versionCode != -1) {
            targetClass.setStaticField("VERSION_CODE", config.versionCode)
        }
        if (config.versionName.isNotBlank()) {
            targetClass.setStaticField("VERSION_NAME", config.versionName)
        }
    }
}