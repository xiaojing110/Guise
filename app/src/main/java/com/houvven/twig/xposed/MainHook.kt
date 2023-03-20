package com.houvven.twig.xposed

import com.houvven.ktxposed.hook.method.service.HookLoadPackageEntrance
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.twig.BuildConfig
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
import de.robv.android.xposed.callbacks.XC_LoadPackage

@Suppress("unused")
class MainHook : HookLoadPackageEntrance {

    override val modulePackageName = BuildConfig.APPLICATION_ID

    override fun loadPackageHook(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedLogger.isDebug = BuildConfig.DEBUG
        XposedLogger.i("start loadPackage: ${lpparam.packageName} [${lpparam.appInfo.name}]")
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
                PrivacyRelated()
            )
        )
    }

}