package com.houvven.twig.xposed.hook

import android.app.Activity
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.houvven.ktxposed.hook.method.afterHookAllMethods
import com.houvven.ktxposed.hook.method.afterHookMethod
import com.houvven.ktxposed.hook.method.callMethod
import com.houvven.twig.xposed.HookValues
import com.houvven.twig.xposed.adapter.TwigHookAdapter

class OtherHooks : TwigHookAdapter {

    override fun onHook() {
        hookBatteryPercentage()
        hookScreenShotBehavior()
    }

    private fun hookBatteryPercentage() {
        val batteryLevel = config.BATTERY_LEVEL
        if (batteryLevel == default.BATTERY_LEVEL) return
        BatteryManager::class.afterHookMethod("getIntProperty", Int::class) {
            if (it.args.first() == BatteryManager.BATTERY_PROPERTY_CAPACITY) {
                it.result = batteryLevel
            }
        }
        Intent::class.afterHookMethod("getIntExtra", String::class, Int::class) {
            if (it.args.first() == BatteryManager.EXTRA_LEVEL) {
                it.result = batteryLevel
            }
        }
    }

    private fun hookScreenShotBehavior() {
        when (config.SCREENSHOTS) {
            default.SCREENSHOTS -> return
            HookValues.SCREEN_SHOTS_DISABLE ->
                Activity::class.afterHookMethod("onCreate", Bundle::class) {
                    val activity = it.thisObject as Activity
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                    activity.window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                }

            HookValues.SCREEN_SHOTS_ENABLE ->
                arrayOf(
                    "setFlags",
                    "setPrivateFlags",
                    "addFlags",
                    "addPrivateFlags",
                    "addSystemFlags"
                ).forEach {
                    Window::class.afterHookAllMethods(it) { param ->
                        if (param.args.first() == WindowManager.LayoutParams.FLAG_SECURE) {
                            param.thisObject.callMethod(
                                "clearFlags",
                                intArrayOf(WindowManager.LayoutParams.FLAG_SECURE)
                            )
                        }
                    }
                }
        }
    }


}