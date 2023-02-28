package com.houvven.guise.ui

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.houvven.androidc.application.DeviceAppProvider
import com.houvven.guise.ui.components.application.AppInfoWithCompose
import com.houvven.guise.ui.components.application.toListWithCompose
import com.houvven.ktxposed.HookStatus
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

@Suppress("unused", "MemberVisibilityCanBePrivate")
@Stable
object AppState {

    /**
     * Xposed 是否已经挂载
     */
    val isHooked by lazy { HookStatus.isHooked }

    /**
     * 设备上已安装的应用列表 [AppInfoWithCompose]
     */
    val apps by derivedStateOf { mutableStateOf(listOf<AppInfoWithCompose>()) }


    /**
     * 应用列表是否正在加载
     */
    val appsIsLoading by derivedStateOf { mutableStateOf(false) }


    /**
     * 刷新应用列表, 会在子线程中执行. 会将 [appsIsLoading] 设置为 true, 执行完毕后设置为 false
     */
    fun refreshApps(context: Context) {
        appsIsLoading.value = true
        thread {
            runBlocking {
                DeviceAppProvider.getInstalledApp(
                    context = context,
                    includeSystemApps = true,
                    needIcon = true
                )
            }.let {
                apps.value = it.toListWithCompose()
            }
            appsIsLoading.value = false
        }
    }


    /**
     * 如果应用列表为空则刷新应用列表
     * @see refreshApps
     */
    fun refreshAppsIfNull(context: Context) {
        if (apps.value.isEmpty()) {
            refreshApps(context)
        }
    }


}