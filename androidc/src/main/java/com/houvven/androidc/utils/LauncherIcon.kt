package com.houvven.androidc.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

object LauncherIcon {

    fun isHide(context: Context, cls: String): Boolean {
        return context.packageManager.getComponentEnabledSetting(
            ComponentName(context, cls)
        ) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }

    fun hide(context: Context, cls: String) {
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, cls),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun show(context: Context, cls: String) {
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, cls),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}