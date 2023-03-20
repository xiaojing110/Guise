package com.houvven.androidc.utils

import android.content.Context
import java.util.Locale
import kotlin.jvm.Throws

object LocaleUtils {

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun fromString(string: String): Locale {
        val split = string.split("_")
        return when (split.size) {
            1 -> Locale(split[0])
            2 -> Locale(split[0], split[1])
            3 -> Locale(split[0], split[1], split[2])
            else -> throw IllegalArgumentException("Invalid locale string: $string")
        }
    }


    fun set(context: Context, locale: Locale) {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

}