package com.houvven.ktxposed.compat

import android.content.SharedPreferences

/**
 * 旧 XSharedPreferences 的最小兼容实现。
 *
 * LSPosed API 102 通过 [io.github.libxposed.api.XposedInterface.getRemotePreferences]
 * 读取模块的 remote preferences，这里做一层旧接口适配。
 */
class XSharedPreferences @JvmOverloads constructor(
    packageName: String,
    private val prefsName: String = packageName
) {

    private fun prefs(): SharedPreferences? = XposedBridge.remotePreferences(prefsName)

    fun getString(key: String, defValue: String?): String? =
        runCatching { prefs()?.getString(key, defValue) }.getOrNull() ?: defValue

    fun getBoolean(key: String, defValue: Boolean): Boolean =
        runCatching { prefs()?.getBoolean(key, defValue) }.getOrNull() ?: defValue

    fun getInt(key: String, defValue: Int): Int =
        runCatching { prefs()?.getInt(key, defValue) }.getOrNull() ?: defValue

    fun getLong(key: String, defValue: Long): Long =
        runCatching { prefs()?.getLong(key, defValue) }.getOrNull() ?: defValue

    fun getFloat(key: String, defValue: Float): Float =
        runCatching { prefs()?.getFloat(key, defValue) }.getOrNull() ?: defValue

    fun getStringSet(key: String, defValues: Set<String>?): Set<String>? =
        runCatching { prefs()?.getStringSet(key, defValues) }.getOrNull() ?: defValues

    fun contains(key: String): Boolean =
        runCatching { prefs()?.contains(key) }.getOrNull() == true

    fun getAll(): Map<String, *> =
        runCatching { prefs()?.all }.getOrNull() ?: emptyMap<String, Any?>()
}
