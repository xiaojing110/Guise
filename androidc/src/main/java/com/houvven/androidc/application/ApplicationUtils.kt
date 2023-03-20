package com.houvven.androidc.application

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.houvven.androidc.command.ShellUtils

object ApplicationUtils {

    fun start(context: Context, packageName: String) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let { ContextCompat.startActivity(context, it, null) }
    }

    fun stop(context: Context, packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${packageName}")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivity(context, intent, null)
    }

    fun stopWithRoot(packageName: String) {
        ShellUtils.exec("am force-stop $packageName", true)
    }

    fun restartWithRoot(context: Context, packageName: String) {
        ShellUtils.exec("am force-stop $packageName", true)
        start(context, packageName)
    }
}