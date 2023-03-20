package com.houvven.twig.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.houvven.twig.ContextAmbient
import com.tencent.mmkv.MMKV

@Suppress("unused")
object AppConfig {
    private val mmkv: MMKV = MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null)

    const val sp_name = "app_config"

    @SuppressLint("WorldReadableFiles", "DEPRECATION")
    private val sp: SharedPreferences =
        try {
            ContextAmbient.current.getSharedPreferences(sp_name, Context.MODE_WORLD_READABLE)
        } catch (e: Exception) {
            ContextAmbient.current.getSharedPreferences(sp_name, Context.MODE_PRIVATE)
        }

    fun mmkv(block: MMKV.() -> Any) = mmkv.block()

    @JvmName("mmkvGet")
    fun <T> mmkv(block: MMKV.() -> T) = mmkv.block()

    @JvmName("spEdit")
    fun spEdit(block: SharedPreferences.Editor.() -> Unit) = sp.edit { block() }

    @JvmName("spGet")
    fun <T> sp(block: SharedPreferences.() -> T) = sp.block()


    const val DEVICE_DB_VERSION = "device_db_version"
    const val AGREE_USER_AGREEMENT = "agree_user_agreement"
    const val LANGUAGE = "language"
    const val MONET_COLORING = "monet_coloring"
    const val THEME_MODE = "theme_mode"
    const val NO_ROOT_WORK = "no_root_work"
    const val GLOBAL_PATTERN = "global_pattern"
    const val ONLY_PRINT_ERROR_LOG = "only_print_error_log"
    const val SHOW_SYSTEM_APP = "show_system_app"
    const val APP_SORT_TYPE = "app_sort_type"
    const val ALSO_SEARCH_PACKAGE_NAME = "also_search_package_name"
    const val APP_RESERVE_SORT = "app_reserve_sort"
}