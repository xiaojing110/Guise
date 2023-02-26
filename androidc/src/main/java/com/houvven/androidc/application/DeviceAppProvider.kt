package com.houvven.androidc.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


/**
 * Device app provider
 * @author Houvven
 */
@Suppress("unused")
object DeviceAppProvider {

    /**
     * 获取设备上已经安装的应用
     *
     * @param context
     * @param includeSystemApps 是否包含系统应用
     * @param needIcon 是否需要图标
     * @see [AppInfo]
     */
    @SuppressLint("InlinedApi")
    @Suppress("DEPRECATION")
    @RequiresPermission(android.Manifest.permission.QUERY_ALL_PACKAGES)
    suspend fun getInstalledApp(
        context: Context,
        includeSystemApps: Boolean,
        needIcon: Boolean
    ): MutableList<AppInfo> {
        return coroutineScope {
            async {
                val pm = context.packageManager
                val installedPackages = pm.getInstalledPackages(0)
                val appList = mutableListOf<AppInfo>()
                for (packageInfo in installedPackages) {
                    val isSystemApp = packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                    if (!includeSystemApps && isSystemApp) {
                        continue
                    }
                    val appInfo = AppInfo(
                        isSystemApp = isSystemApp,
                        label = packageInfo.applicationInfo.loadLabel(pm).toString(),
                        packageName = packageInfo.packageName,
                        icon = if (needIcon) packageInfo.applicationInfo.loadIcon(pm) else null,
                        versionName = packageInfo.versionName,
                        versionCode = packageInfo.versionCode,
                        firstInstallTime = packageInfo.firstInstallTime,
                        lastUpdateTime = packageInfo.lastUpdateTime,
                        dataDir = packageInfo.applicationInfo.dataDir,
                        sourceDir = packageInfo.applicationInfo.sourceDir
                    )
                    appList.add(appInfo)
                }
                appList
            }.await()
        }
    }
}