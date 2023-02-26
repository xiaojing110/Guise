package com.houvven.androidc.application

import android.graphics.drawable.Drawable

/**
 * App info
 *
 * @property isSystemApp
 * @property label
 * @property packageName
 * @property icon
 * @property versionName
 * @property versionCode
 * @property firstInstallTime
 * @property lastUpdateTime
 * @property dataDir
 * @property sourceDir
 */
@Suppress("unused")
data class AppInfo(
    /**
     * 是否是系统应用
     */
    val isSystemApp: Boolean,
    /**
     * 应用名
     */
    val label: String,
    /**
     * 包名
     */
    val packageName: String,
    /**
     * 应用图标
     */
    val icon: Drawable?,
    /**
     * 应用版本名
     */
    val versionName: String,
    /**
     * 应用版本号
     */
    val versionCode: Int,
    /**
     * 应用首次安装时间
     */
    val firstInstallTime: Long,
    /**
     * 应用最后一次更新时间
     */
    val lastUpdateTime: Long,
    /**
     * 数据目录
     */
    val dataDir: String,
    /**
     * APK文件路径
     */
    val sourceDir: String
)