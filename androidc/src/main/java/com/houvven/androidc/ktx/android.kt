@file:JvmName("AndroidKtx")
@file:Suppress("unused")

package com.houvven.androidc.ktx

import android.content.Context
import android.widget.Toast

/**
 * 当前正在显示的Toast
 */
private lateinit var currentToast: Toast


/**
 * 创建新的Toast
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(this, message, duration)
    toast.show()
    currentToast = toast
}

/**
 * 如果存在正在显示的Toast，则更新内容，否则创建新的Toast
 */
@Suppress("DEPRECATION")
fun Context.showToastIfVisible(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (currentToast.view?.isShown == true) {
        currentToast.setText(message)
        currentToast.duration = duration
    } else {
        showToast(message, duration)
    }
}

/**
 * 如果存在正在显示的Toast，则取消显示正在显示的Toast，创建新的Toast
 */
@Suppress("DEPRECATION")
fun Context.showToastIfDismissible(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (currentToast.view?.isShown == true) {
        currentToast.cancel()
    }
    showToast(message, duration)
}
