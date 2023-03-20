package com.houvven.twig.xposed.hook.basic

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import com.houvven.ktxposed.hook.method.afterHookMethod
import com.houvven.ktxposed.hook.method.beforeHookAllMethods
import com.houvven.ktxposed.hook.method.replaceAllMethodsResult
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.ktxposed.hook.method.setStaticField
import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import java.util.Locale
import java.util.TimeZone

class SystemInfoHook : TwigHookAdapter {


    override fun onHook() {
        hookInformationAboutBuildClass()
        hookLanguage()
        hookTimeZone()
        hookUsbDebugState()
    }


    private fun hookInformationAboutBuildClass() = config.run {
        listOf(
            API to arrayOf(default.API, "SDK_INT"),
            RELEASE to arrayOf(default.RELEASE, "RELEASE"),
            FINGERPRINT to arrayOf(default.FINGERPRINT, "BASE_OS")
        ).forEach { (value, names) ->
            if (value == names.first()) return@forEach
            names.forEachIndexed { index, s ->
                if (index == 0) return@forEachIndexed
                runXposedCatching {
                    Build.VERSION::class.setStaticField(s.toString(), value)
                }
            }
        }

        if (FINGERPRINT != default.FINGERPRINT)
            Build::class.setStaticField("FINGERPRINT", FINGERPRINT)
    }


    private fun hookLanguage() {
        if (config.LANGUAGE == default.LANGUAGE) return

        val split = config.LANGUAGE.split("_")
        val locale = Locale(split.first(), split.getOrElse(1) { "" })

        Locale::class.run {
            replaceAllMethodsResult("getDefault") { locale }
            replaceAllMethodsResult("initDefault") { locale }
            beforeHookAllMethods("setDefault") { param ->
                param.args.run { this[indexOf(find { it is Locale })] = locale }
            }
        }

        Configuration::class.replaceMethodResult("getLocales") { LocaleList(locale) }

        Resources::class.run {
            afterHookMethod("getConfiguration") {
                val result = it.result as Configuration
                it.result = result.apply {
                    setLocale(locale)
                    setLocales(LocaleList(locale))
                }
            }
        }
    }

    private fun hookTimeZone() {
        if (config.TIMEZONE == default.TIMEZONE) return
        arrayOf(
            android.icu.util.TimeZone::class,
            TimeZone::class
        ).forEach { kClass ->
            kClass.replaceAllMethodsResult("getDefault") { TimeZone.getTimeZone(config.TIMEZONE) }
            kClass.beforeHookAllMethods("setDefault") { param ->
                param.args.run {
                    this[indexOf(find { it is TimeZone })] = TimeZone.getTimeZone(config.TIMEZONE)
                }
            }
        }
    }

    private fun hookUsbDebugState() {
        if (config.USB_DEBUGGING == default.USB_DEBUGGING) return
        // Settings.Secure.getInt()
        Settings.Secure::class.beforeHookAllMethods("getStringForUser") { param ->
            if (param.args[1] == Settings.Secure.ADB_ENABLED) {
                param.result = config.USB_DEBUGGING.toString()
            }
        }
        // Settings.Global.getInt()
        Settings.Global::class.beforeHookAllMethods("getStringForUser") { param ->
            if (param.args[1] == Settings.Global.ADB_ENABLED) {
                param.result = config.USB_DEBUGGING.toString()
            }
        }
    }
}
