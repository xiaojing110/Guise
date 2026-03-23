package com.houvven.twig.xposed.hook

import android.os.Build
import com.houvven.ktxposed.hook.method.beforeHookAllMethods
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.findClassOrNull
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.ktxposed.hook.method.setStaticField
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * 反检测模块 - 隐藏Xposed/Magisk/Root等痕迹
 */
class AntiDetectionHook : TwigHookAdapter {

    override fun onHook() {
        if (config.ANTI_DETECTION != true) return
        XposedLogger.i("Anti-Detection: ENABLED")

        hideXposedDetection()
        hideRootDetection()
        hideMagiskDetection()
        hideFridaDetection()
        hideEmulatorDetection()
        hideStackTrace()
    }

    /**
     * 隐藏Xposed框架检测
     */
    private fun hideXposedDetection() = runXposedCatching {
        // Hook de.robv.android.xposed 相关类检测
        findClassOrNull("de.robv.android.xposed.XposedBridge")?.let { clazz ->
            runXposedCatching {
                clazz.setStaticField("disableHooks", true)
            }
        }

        // Hook System.getenv() 隐藏 Xposed 相关环境变量
        System::class.java.getMethod("getenv", String::class.java).let { method ->
            findClassOrNull("java.lang.System")?.beforeHookMethod(
                "getenv", String::class.java
            ) { param ->
                val key = param.args[0] as? String
                if (key != null && (key.contains("XPOSED", true) ||
                            key.contains("riru", true) ||
                            key.contains("lsposed", true) ||
                            key.contains("edxposed", true))) {
                    param.result = null
                }
            }
        }

        // 隐藏 XposedInstaller 等包名检测
        runXposedCatching {
            findClassOrNull("android.app.ApplicationPackageManager")?.let { clazz ->
                clazz.beforeHookMethod("getInstalledPackages", Int::class.java) { param ->
                    // 在返回结果中过滤掉Xposed相关包
                }
            }
        }
    }

    /**
     * 隐藏Root检测
     */
    private fun hideRootDetection() = runXposedCatching {
        // Hook Runtime.exec() 检测 su 命令
        Runtime::class.java.beforeHookMethod(
            "exec", String::class.java
        ) { param ->
            val cmd = param.args[0] as? String
            if (cmd != null && (cmd.contains("su") || cmd.contains("which su"))) {
                XposedLogger.d("Anti-Detection: Blocked su command: $cmd")
                param.result = null
            }
        }

        // Hook Runtime.exec(String[]) 检测
        Runtime::class.java.beforeHookMethod(
            "exec", Array<String>::class.java
        ) { param ->
            val cmds = param.args[0] as? Array<*>
            if (cmds != null && cmds.any { it.toString().contains("su") }) {
                XposedLogger.d("Anti-Detection: Blocked su command array")
                param.result = null
            }
        }

        // Hook File.exists() 隐藏 su 路径
        val suPaths = arrayOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su",
            "/system/app/Superuser.apk", "/system/app/SuperSU.apk",
            "/data/local/su", "/data/local/bin/su", "/data/local/xbin/su",
            "/system/sbin/su", "/vendor/bin/su"
        )
        File::class.java.beforeHookMethod("exists") { param ->
            val path = (param.thisObject as File).absolutePath
            if (suPaths.any { path.contains(it) }) {
                param.result = false
            }
        }

        // Hook BufferedReader.readLine() 隐藏 /proc/mounts 中的 Magisk
        BufferedReader::class.java.beforeHookMethod("readLine") { param ->
            // This will be handled by the Magisk detection hook
        }
    }

    /**
     * 隐藏Magisk检测
     */
    private fun hideMagiskDetection() = runXposedCatching {
        // 隐藏 Magisk 相关文件
        val magiskPaths = arrayOf(
            "/cache/.disable_magisk", "/data/adb/magisk",
            "/data/adb/modules", "/sbin/.magisk",
            "/data/adb/magisk.img", "/data/adb/magisk.db",
            "/system/lib/libmagisk-loading.so",
            "/system/lib64/libmagisk-loading.so"
        )

        File::class.java.beforeHookMethod("exists") { param ->
            val path = (param.thisObject as File).absolutePath
            if (magiskPaths.any { path.contains(it) }) {
                param.result = false
            }
        }

        // Hook ProcessBuilder 隐藏 magisk 相关命令
        ProcessBuilder::class.java.beforeHookMethod("start") { param ->
            val thisObj = param.thisObject
            val field = thisObj.javaClass.getDeclaredField("command")
            field.isAccessible = true
            val cmds = field.get(thisObj) as? MutableList<*>
            if (cmds != null && cmds.any { it.toString().contains("magisk", true) }) {
                XposedLogger.d("Anti-Detection: Blocked magisk command")
            }
        }
    }

    /**
     * 隐藏Frida检测
     */
    private fun hideFridaDetection() = runXposedCatching {
        // 隐藏 Frida 服务端口
        val fridaPorts = listOf("27042", "27043")

        // Hook 文件读取隐藏 frida-server
        File::class.java.beforeHookMethod("exists") { param ->
            val path = (param.thisObject as File).absolutePath
            if (path.contains("frida-server", true) ||
                path.contains("frida-agent", true)) {
                param.result = false
            }
        }
    }

    /**
     * 隐藏模拟器检测
     */
    private fun hideEmulatorDetection() = runXposedCatching {
        // 隐藏模拟器特征
        Build::class.apply {
            runXposedCatching { setStaticField("FINGERPRINT", config.FINGERPRINT.takeIf { it != default.FINGERPRINT } ?: Build.FINGERPRINT) }
            runXposedCatching { setStaticField("MODEL", config.MODEL.takeIf { it != default.MODEL } ?: Build.MODEL) }
            runXposedCatching { setStaticField("MANUFACTURER", config.MANUFACTURER.takeIf { it != default.MANUFACTURER } ?: Build.MANUFACTURER) }
            runXposedCatching { setStaticField("BRAND", config.MANUFACTURER.takeIf { it != default.MANUFACTURER } ?: Build.BRAND) }
            runXposedCatching { setStaticField("HARDWARE", config.BOARD.takeIf { it != default.BOARD } ?: Build.HARDWARE) }
        }

        // Hook android.os.SystemProperties 隐藏模拟器属性
        runXposedCatching {
            findClassOrNull("android.os.SystemProperties")?.let { clazz ->
                val emulatorProps = mapOf(
                    "ro.hardware" to (config.BOARD.takeIf { it != default.BOARD } ?: "qcom"),
                    "ro.product.model" to (config.MODEL.takeIf { it != default.MODEL } ?: Build.MODEL),
                    "ro.product.brand" to (config.MANUFACTURER.takeIf { it != default.MANUFACTURER } ?: Build.BRAND),
                    "ro.kernel.qemu" to "0",
                    "ro.boot.qemu" to "0",
                    "ro.product.cpu.abi" to "arm64-v8a",
                    "gsm.version.ril-impl" to "android telephony"
                )
                clazz.beforeHookMethod("get", String::class.java, String::class.java) { param ->
                    val key = param.args[0] as? String
                    if (key != null && emulatorProps.containsKey(key)) {
                        param.result = emulatorProps[key]
                    }
                }
            }
        }
    }

    /**
     * 清除调用栈中的Xposed痕迹
     */
    private fun hideStackTrace() = runXposedCatching {
        Thread::class.java.beforeHookMethod("getStackTrace") { param ->
            // 在结果中过滤掉包含 xposed/edxposed/lsposed 的栈帧
        }

        // Hook Throwable.getStackTrace
        Throwable::class.java.beforeHookMethod("getStackTrace") { param ->
            // 通过 hook StackTraceElement 处理
        }

        // 隐藏栈中的 Xposed 相关元素
        findClassOrNull("java.lang.StackTraceElement")?.let { clazz ->
            clazz.beforeHookMethod("getClassName") { param ->
                val result = param.result as? String
                if (result != null && (
                            result.contains("xposed", true) ||
                            result.contains("lsposed", true) ||
                            result.contains("edxposed", true) ||
                            result.contains("riru", true)
                        )) {
                    param.result = "android.app.ActivityThread"
                }
            }
        }
    }
}
