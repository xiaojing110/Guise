package com.houvven.guise.module

import com.houvven.lib.command.ShellActuators

object SystemProp {

    @JvmStatic
    val abi: String
        get() {
            val abi = ShellActuators.exec("getprop ro.product.cpu.abi", true)
            return if (abi.isSuccess) abi.getOrNull()?.trim() ?: "unknown" else "unknown"
        }


    @JvmStatic
    val architecture: String
        get() = when (abi) {
            "arm64-v8a" -> "arm64"
            "armeabi-v7a" -> "arm"
            "x86_64" -> "x86_64"
            "x86" -> "x86"
            else -> "unknown"
        }

}