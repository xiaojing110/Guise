package com.houvven.twig.xposed.hook

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import com.houvven.ktxposed.LocalHookParam
import com.houvven.ktxposed.hook.method.afterHookAllConstructors
import com.houvven.ktxposed.hook.method.afterHookMethod
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.callStaticMethod
import com.houvven.ktxposed.hook.method.findClass
import com.houvven.ktxposed.hook.method.findClassOrNull
import com.houvven.ktxposed.hook.method.getInstanceField
import com.houvven.ktxposed.hook.method.setInstanceField
import com.houvven.ktxposed.hook.method.setStaticField
import com.houvven.ktxposed.hook.method.setStaticFieldIfExits
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import java.lang.reflect.Proxy

object AppInfoHook : TwigHookAdapter {

    private val pn: String = LocalHookParam.lpparam.appInfo.packageName

    @SuppressLint("PrivateApi")
    override fun onHook(): Unit = config.run {
        if (APP_VERSION_CODE == default.APP_VERSION_CODE && APP_VERSION_NAME == default.APP_VERSION_NAME) {
            return@run
        }
        if (pn == "com.google.android.webview") return@run

        findClassOrNull("$pn.BuildConfig") {
            if (APP_VERSION_CODE != default.APP_VERSION_CODE) {
                setStaticFieldIfExits("VERSION_CODE", APP_VERSION_CODE)
            }
            if (APP_VERSION_NAME != default.APP_VERSION_NAME) {
                setStaticFieldIfExits("VERSION_NAME", APP_VERSION_NAME)
            }
        }

        runXposedCatching {
            findClass("android.app.ActivityThread") {
                val currentAT = callStaticMethod("currentActivityThread").getOrThrow()
                val ipm = currentAT.getInstanceField<Any>("sPackageManager")
                val ipmClass =
                    Class.forName("android.content.pm.IPackageManager") ?: return@findClass
                val proxy = newProxy(ipmClass, ipm)
                setStaticField("sPackageManager", proxy)
                currentAT.setInstanceField("sPackageManager", proxy)
                Application::class.afterHookMethod("attach", Context::class) {
                    val context = it.args[0] as Context
                    context.packageManager.setInstanceField(
                        "mPM",
                        newProxy(ipmClass, context.packageManager.getInstanceField("mPM"))
                    )
                }
            }
        }

        PackageInfo::class.afterHookAllConstructors {
            val pi = it.thisObject as PackageInfo
            repairPackageInfo(pi)
        }

        PackageInfo::class.beforeHookMethod("getLongVersionCode") {
            val pi = it.thisObject as PackageInfo
            if (pi.packageName == pn) {
                if (APP_VERSION_CODE != default.APP_VERSION_CODE) {
                    it.result = APP_VERSION_CODE.toLong()
                }
            }
        }

    }


    private fun newProxy(clazz: Class<*>, obj: Any): Any {
        return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz),
        ) { _, method, args ->
            if (args != null && args.any { it != "com.google.android.webview" && it == pn }) {
                if (method.name == "getPackageInfo") {
                    val packageInfo = method.invoke(obj, *args) as PackageInfo
                    XposedLogger.d("getPackageInfo: $packageInfo")
                    return@newProxyInstance repairPackageInfo(packageInfo)
                }
            } else if (method.name == "isSafeMode") {
                return@newProxyInstance true
            }
            return@newProxyInstance kotlin.runCatching { method.invoke(obj, *args) }
                .getOrNull()
        }
    }


    private fun repairPackageInfo(pi: PackageInfo): PackageInfo = config.run {
        if (pi.packageName == pn) {
            if (APP_VERSION_CODE != default.APP_VERSION_CODE) {
                pi.versionCode = APP_VERSION_CODE
                pi.setInstanceField("versionCodeMajor", APP_VERSION_CODE)
                pi.setInstanceField("baseRevisionCode", APP_VERSION_CODE)
                pi.applicationInfo.setInstanceField("versionCode", APP_VERSION_CODE)
                pi.applicationInfo.setInstanceField("longVersionCode", APP_VERSION_CODE)
            }
            if (APP_VERSION_NAME != default.APP_VERSION_NAME) {
                pi.versionName = APP_VERSION_NAME
            }
        }
        pi
    }
}