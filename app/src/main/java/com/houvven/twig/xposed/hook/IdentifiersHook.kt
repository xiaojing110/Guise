package com.houvven.twig.xposed.hook

import android.content.ContentResolver
import android.provider.Settings
import android.telephony.TelephonyManager
import com.houvven.ktxposed.hook.method.afterHookAllMethods
import com.houvven.ktxposed.hook.method.afterHookMethod
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.twig.TwigStore
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import de.robv.android.xposed.XC_MethodHook

class IdentifiersHook : TwigHookAdapter {

    override fun onHook() {
        hookSSAID()
        hookAboutTelId()
        hookPhoneNumber()
    }

    private fun hookSSAID() {
        fun hook(param: XC_MethodHook.MethodHookParam) {
            if (config.DEVICE_ID.isBlank()) {
                XposedLogger.d("${lpparam.packageName} get android id, but is blank")
                XposedLogger.d("Web view processName: ${lpparam.processName}")
                val c = TwigStore.XPOSED.get(lpparam.processName)
                if (c.DEVICE_ID != default.DEVICE_ID) param.result = c.DEVICE_ID
                else XposedLogger.d("Web view processName: ${lpparam.processName} is not in config")
            } else {
                param.result = config.DEVICE_ID
            }
        }

        Settings.Secure::class.afterHookMethod(
            methodName = "getStringForUser",
            ContentResolver::class,
            String::class,
            Int::class
        ) { param ->
            if (param.args[1] == Settings.Secure.ANDROID_ID) {
                hook(param)
            }
        }

        Settings.System::class.afterHookMethod(
            methodName = "getStringForUser",
            ContentResolver::class, String::class, Int::class
        ) { param ->
            if (param.args[1] == Settings.System.ANDROID_ID) {
                hook(param)
            }
        }
    }

    private fun hookAboutTelId() {
        if (config.IMEI == default.IMEI) return
        TelephonyManager::class.run {
            afterHookAllMethods("getImei") { it.result = config.IMEI }
            afterHookAllMethods("getDeviceId") { it.result = config.IMEI }
        }
    }

    private fun hookPhoneNumber() {
        if (config.PHONE_NUMBER == default.PHONE_NUMBER) return
        TelephonyManager::class.run {
            afterHookAllMethods("getLine1Number") { it.result = config.PHONE_NUMBER }
        }
    }
}