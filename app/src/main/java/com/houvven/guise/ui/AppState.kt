package com.houvven.guise.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.houvven.guise.ui.components.application.AppInfoWithCompose
import com.houvven.ktxposed.HookStatus

@Suppress("unused")
object AppState {
    val isHooked by lazy { HookStatus.isHooked }

    /**
     * 设备上已安装的应用列表 [AppInfoWithCompose]
     */
    val apps by derivedStateOf { mutableStateOf(listOf<AppInfoWithCompose>()) }


}