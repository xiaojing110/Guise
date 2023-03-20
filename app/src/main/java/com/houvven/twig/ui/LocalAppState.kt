package com.houvven.twig.ui

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.houvven.androidc.application.DeviceAppProvider
import com.houvven.androidc.ktx.ifFalse
import com.houvven.ktxposed.HookStatus
import com.houvven.twig.config.AppConfig
import com.houvven.twig.ui.components.application.AppInfoSortType
import com.houvven.twig.ui.components.application.AppInfoWithCompose
import com.houvven.twig.ui.components.application.toAppInfoWithCompose
import com.houvven.twig.ui.theme.ThemeMode
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

@Suppress("unused", "MemberVisibilityCanBePrivate")
@Stable
internal object LocalAppState {

    object Apps {
        /**
         * App列表
         */
        val current by derivedStateOf { mutableStateOf((listOf<AppInfoWithCompose>())) }

        /**
         * App列表加载状态
         */
        val appsIsLoading by derivedStateOf { mutableStateOf(false) }

        /**
         * App列表至少加载完成一次
         */
        val appsIsLoaded by derivedStateOf { mutableStateOf(false) }

        val sortType by derivedStateOf {
            mutableStateOf(AppConfig.run {
                mmkv<String> {
                    decodeString(APP_SORT_TYPE, AppInfoSortType.NAME.name)!!
                }.let { AppInfoSortType.valueOf(it) }
            })
        }

        val showSystemApps by derivedStateOf {
            mutableStateOf(AppConfig.run {
                mmkv<Boolean> { decodeBool(SHOW_SYSTEM_APP, false) }
            })
        }

        val reverseOrder by derivedStateOf {
            mutableStateOf(AppConfig.run {
                mmkv<Boolean> { decodeBool(APP_RESERVE_SORT, false) }
            })
        }

        /**
         * 也可以按包名搜索
         */
        val alsoSearchByPackageName by derivedStateOf {
            mutableStateOf(AppConfig.run {
                mmkv<Boolean> { decodeBool(ALSO_SEARCH_PACKAGE_NAME, false) }
            })
        }

        fun refresh(context: Context) {
            appsIsLoading.value = true
            thread {
                runBlocking {
                    DeviceAppProvider.getInstalledApp(
                        context = context,
                        includeSystemApps = true,
                        needIcon = true
                    )
                }.let {
                    current.value = it.toAppInfoWithCompose()
                }
                appsIsLoaded.value ifFalse { appsIsLoaded.value = true }
                appsIsLoading.value = false
            }
        }

        fun refreshIfNull(context: Context) {
            if (current.value.isEmpty()) refresh(context)
        }
    }

    /**
     * 用户协议是否已经同意
     */
    val isAgreeUserAgreement by derivedStateOf {
        mutableStateOf(AppConfig.run { mmkv<Boolean> { decodeBool(AGREE_USER_AGREEMENT, false) } })
    }

    val language by derivedStateOf {
        mutableStateOf(AppConfig.run { mmkv<String> { decodeString(LANGUAGE, "")!! } })
    }

    /**
     * 应用主题
     */
    val themeMode by derivedStateOf {
        mutableStateOf(AppConfig.run {
            mmkv<String> { decodeString(THEME_MODE, ThemeMode.AUTO.label)!! }
        })
    }

    /**
     * 是否启用莫奈取色
     */
    val monetColoring by derivedStateOf {
        mutableStateOf(AppConfig.run { mmkv<Boolean> { decodeBool(MONET_COLORING, false) } })
    }

    /**
     * 是否忽略 Xposed 挂载状态
     */
    val noRootWork = mutableStateOf(AppConfig.run { sp { getBoolean(NO_ROOT_WORK, false) } })

    /**
     * Xposed 是否已经挂载
     */
    val isHooked by mutableStateOf(HookStatus.isHooked || noRootWork.value)


    /**
     * 是否启用全局模式
     */
    val isGlobalPattern by derivedStateOf {
        mutableStateOf(AppConfig.run { sp { getBoolean(GLOBAL_PATTERN, false) } })
    }

}