package com.houvven.twig

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import com.houvven.ktxposed.LocalHookParam
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.twig.config.AppConfig
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.ui.LocalAppState
import de.robv.android.xposed.XSharedPreferences
import java.io.File


@Suppress("unused", "MemberVisibilityCanBePrivate")
object TwigStore {

    private const val store_name = "twig"

    private const val global_key = "_global"

    /* private const val DISABLE_APPS_SP_NAME = "disable_apps_sp"

    private const val DISABLE_APP_MEDIA_FILE_NAME = "disable" */

    object XPOSED {
        private val appConfig
            get() = XSharedPreferences(BuildConfig.APPLICATION_ID, AppConfig.sp_name)

        private val isGlobalPattern get() = appConfig.getBoolean(AppConfig.GLOBAL_PATTERN, false)

        private val isNoRoot get() = appConfig.getBoolean(AppConfig.NO_ROOT_WORK, false)


        private val xsp by lazy { XSharedPreferences(BuildConfig.APPLICATION_ID, store_name) }

        /* private val disableAppsSp by lazy {
            XSharedPreferences(BuildConfig.APPLICATION_ID, DISABLE_APPS_SP_NAME)
        } */

        private fun getFromMedia(packageName: String) = Environment.getExternalStorageDirectory()
            .resolve("Android/media/${packageName}/$store_name.json")
            .readText()

        private fun getFromSp(packageName: String) = xsp.getString(packageName, "")!!

        private fun getGlobal() = xsp.getString(global_key, "")!!

        fun get(): TwigConfig.Config {
            return get(LocalHookParam.lpparam.packageName)
        }

        fun get(packageName: String): TwigConfig.Config {
            XposedLogger.d("Twig config {global: $isGlobalPattern; no root: $isNoRoot}")
            var config =
                if (isGlobalPattern && !isNoRoot) getGlobal()
                else getFromSp(packageName)
            if (config.isBlank()) {
                XposedLogger.d("Twig config is blank from global and sp.")
                config = getFromMedia(packageName)
            }
            return try {
                TwigConfig.Config.fromJSON(config)
            } catch (e: Exception) {
                TwigConfig.Config.default
            }
        }

        /* fun isEnable(packageName: String): Boolean {
            return if (isNoRoot) {
                Environment.getExternalStorageDirectory()
                    .resolve("Android/media/${packageName}/$DISABLE_APP_MEDIA_FILE_NAME").exists()
                    .not()
            } else {
                disableAppsSp.getBoolean(packageName, true)
            }
        } */

    }

    object APP {
        private const val TAG = "TwigStore.APP"

        @Suppress("DEPRECATION", "WorldReadableFiles")
        private val sp by lazy {
            try {
                ContextAmbient.current.getSharedPreferences(store_name, Context.MODE_WORLD_READABLE)
            } catch (e: UninitializedPropertyAccessException) {
                ContextAmbient.current.getSharedPreferences(store_name, Context.MODE_PRIVATE)
            }
        }

        val all by lazy {
            val value by derivedStateOf { mutableStateOf(listOf<String>()) }

            if (LocalAppState.noRootWork.value) {
                val list = mutableListOf<String>()
                Environment.getExternalStorageDirectory().resolve("Android/media").listFiles()
                    ?.forEach { if (it.resolve("twig.json").exists()) list.add(it.name) }
                value.value = list
            } else value.value = sp.all.keys.toList()
            value
        }

        /* @Suppress("DEPRECATION", "WorldReadableFiles")
        val disableAppsSp: SharedPreferences by lazy {
            try {
                ContextAmbient.current.getSharedPreferences(
                    DISABLE_APPS_SP_NAME,
                    Context.MODE_WORLD_READABLE
                )
            } catch (e: UninitializedPropertyAccessException) {
                ContextAmbient.current.getSharedPreferences(
                    DISABLE_APPS_SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
        } */

        private fun getFromMedia(packageName: String) = try {
            Environment.getExternalStorageDirectory()
                .resolve("Android/media/$packageName/$store_name.json").readText()
        } catch (e: Exception) {
            ""
        }

        private fun getFromSp(packageName: String) = sp.getString(packageName, "")!!

        fun getGlobalFromSp(): TwigConfig.Config {
            return try {
                TwigConfig.Config.fromJSON(sp.getString(global_key, "")!!)
            } catch (e: Exception) {
                TwigConfig.Config.default
            }
        }

        fun get(packageName: String): TwigConfig.Config {
            val config = if (LocalAppState.noRootWork.value) getFromMedia(packageName)
            else getFromSp(packageName)
            return try {
                TwigConfig.Config.fromJSON(config).copy(packageName = packageName)
            } catch (e: Exception) {
                TwigConfig.Config.default.copy(packageName = packageName)
            }
        }

        private fun saveToGlobal(config: TwigConfig.Config) {
            sp.edit { putString(global_key, config.toJSON()) }
        }

        private fun saveToSp(packageName: String, config: TwigConfig.Config) {
            if (config.isEnable) sp.edit { putString(packageName, config.toJSON()) }
            else sp.edit { remove(packageName) }
        }

        private fun saveToMedia(packageName: String, config: TwigConfig.Config) {
            Log.d(TAG, "saveToMedia: $packageName  $config")
            val file = Environment.getExternalStorageDirectory()
                .resolve("Android/media/$packageName/$store_name.json")

            if (file.parentFile?.exists()?.not() == true) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }

            if (!config.isEnable) {
                runCatching { file.delete() }
                return
            }

            file.writeText(config.toJSON())
        }

        fun save(config: TwigConfig.Config) {
            val packageName = config.packageName
            if (config.isEnable && LocalAppState.isGlobalPattern.value.not())
                all.value = all.value
                    .toMutableList()
                    .apply { if (contains(packageName).not()) add(packageName) }
            else
                all.value = all.value.toMutableList().apply { remove(packageName) }

            if (LocalAppState.isGlobalPattern.value && !LocalAppState.noRootWork.value)
                saveToGlobal(config)
            else if (LocalAppState.noRootWork.value)
                saveToMedia(packageName, config)
            else
                saveToSp(packageName, config)
        }

        /* fun setState(packageName: String, state: Boolean) {
            if (LocalAppState.noRootWork.value) {
                if (state) {
                    Environment.getExternalStorageDirectory()
                        .resolve("Android/media/$packageName/$DISABLE_APP_MEDIA_FILE_NAME").delete()
                } else {
                    Environment.getExternalStorageDirectory()
                        .resolve("Android/media/$packageName/$DISABLE_APP_MEDIA_FILE_NAME").let {
                            if (it.parentFile?.exists()?.not() == true) {
                                it.parentFile?.mkdirs()
                            }
                            it.createNewFile()
                        }
                }
            } else {
                disableAppsSp.edit { putBoolean(packageName, state) }
            }
        }

        fun isEnable(packageName: String): Boolean {
            return if (LocalAppState.noRootWork.value) {
                Environment.getExternalStorageDirectory()
                    .resolve("Android/media/$packageName/$DISABLE_APP_MEDIA_FILE_NAME").exists()
                    .not()
            } else {
                disableAppsSp.getBoolean(packageName, true)
            }
        } */
    }

}