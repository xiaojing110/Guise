package com.houvven.guise.xposed.hook

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.houvven.guise.xposed.LoadPackageHandler
import com.houvven.ktx_xposed.hook.afterHookAllMethods
import com.houvven.ktx_xposed.hook.findClassIfExists
import com.houvven.ktx_xposed.hook.lppram
import com.houvven.ktx_xposed.hook.setStaticField
import com.houvven.ktx_xposed.logger.XposedLogger

class BuildConfigHook : LoadPackageHandler {

    override fun onHook() {
        val name = lppram.packageName
        val targetClass = findClassIfExists("$name.BuildConfig")

        if (targetClass == null) {
            XposedLogger.i("BuildConfigHook: $name.BuildConfig not found.")
            return
        }

        PackageManager::class.java.run {
            afterHookAllMethods("getPackageInfo") {
                if (it.args.contains(lppram.packageName)) {
                    it.result = PackageInfo().apply {
                        versionName = config.versionName
                        versionCode = config.versionCode
                        longVersionCode = config.versionCode.toLong()
                    }
                }
            }
        }


        if (config.versionCode != -1) {
            targetClass.setStaticField("VERSION_CODE", config.versionCode)
        }
        if (config.versionName.isNotBlank()) {
            targetClass.setStaticField("VERSION_NAME", config.versionName)
        }
    }
}