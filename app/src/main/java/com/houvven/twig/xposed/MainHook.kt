package com.houvven.twig.xposed

import android.app.Application
import android.os.Build
import android.os.Environment
import com.houvven.ktxposed.HookStatus
import com.houvven.ktxposed.LocalHookParam
import com.houvven.ktxposed.compat.XC_LoadPackage
import com.houvven.ktxposed.compat.XC_MethodHook
import com.houvven.ktxposed.compat.XposedBridge
import com.houvven.ktxposed.hook.method.service.HookLoadPackageEntrance
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.twig.BuildConfig
import com.houvven.twig.xposed.hook.AntiDetectionHook
import com.houvven.twig.xposed.hook.AntiFingerprintHook
import com.houvven.twig.xposed.hook.AppInfoHook
import com.houvven.twig.xposed.hook.IdentifiersHook
import com.houvven.twig.xposed.hook.NetworkInfoHook
import com.houvven.twig.xposed.hook.OtherHooks
import com.houvven.twig.xposed.hook.PrivacyRelated
import com.houvven.twig.xposed.hook.SimInfoHook
import com.houvven.twig.xposed.hook.basic.DeviceInfoHook
import com.houvven.twig.xposed.hook.basic.HardwareInfoHook
import com.houvven.twig.xposed.hook.basic.SystemInfoHook
import com.houvven.twig.xposed.hook.location.LocationHook
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import java.io.File

/**
 * LSPosed API 102 模块入口。
 */
@Suppress("unused")
class MainHook : XposedModule(), HookLoadPackageEntrance {

    override val modulePackageName = BuildConfig.APPLICATION_ID

    private var moduleActivated = false

    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        XposedBridge.attach(this)
        if (param.processName == modulePackageName) {
            activateModule()
        }
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        XposedBridge.attach(this)

        if (param.packageName == modulePackageName) {
            activateModule()
            return
        }

        val lpparam = XC_LoadPackage.LoadPackageParam(
            packageName = param.packageName,
            processName = currentProcessName(param.packageName),
            classLoader = param.defaultClassLoader,
            appInfo = param.applicationInfo,
            isFirstApplication = param.isFirstPackage
        )
        LocalHookParam.lpparam = lpparam
        loadPackageHook(lpparam)
    }

    override fun loadPackageHook(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedLogger.init(
            packageName = lpparam.packageName,
            debug = BuildConfig.DEBUG
        )
        XposedLogger.i("start loadPackage: ${lpparam.packageName} [${lpparam.appInfo.name}]")
        markActive(lpparam.packageName)
        LocalHookConfig.updateValue()

        doHookLoadPackages(
            listOf(
                DeviceInfoHook(),
                SystemInfoHook(),
                HardwareInfoHook(),
                NetworkInfoHook(),
                SimInfoHook(),
                LocationHook(),
                AppInfoHook,
                IdentifiersHook(),
                OtherHooks(),
                PrivacyRelated(),
                AntiDetectionHook(),
                AntiFingerprintHook()
            )
        )
    }

    private fun markActive(packageName: String) {
        runCatching {
            val dir = Environment.getExternalStorageDirectory()
                .resolve("Android/media/$packageName")
            if (!dir.exists()) dir.mkdirs()
            File(dir, "twig_active").writeText(System.currentTimeMillis().toString())
        }
    }

    /**
     * 让应用自己的 UI 判断模块已经激活。
     */
    private fun activateModule() {
        if (moduleActivated) return
        moduleActivated = true
        runCatching {
            val method = HookStatus.Companion::class.java.getDeclaredMethod("isHooked")
            method.isAccessible = true
            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = true
                }
            })
            XposedLogger.d("HookStatus isHooked: true")
        }
    }

    private fun currentProcessName(packageName: String): String = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Application.getProcessName()
        } else {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentProcessName")
                .invoke(null) as String
        }
    }.getOrNull() ?: packageName
}
