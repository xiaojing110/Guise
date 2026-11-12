package com.houvven.ktxposed.log

import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 统一日志出口。
 *
 * 挂载在目标应用进程中的模块无法被宿主应用直接读取内存，因此日志会同时写入
 * `/sdcard/Android/media/<目标包名>/twig.log`，宿主应用（Guise）在获得
 * MANAGE_EXTERNAL_STORAGE 权限后即可在「日志」页汇总展示。
 */
@Suppress("unused")
object XposedLogger {

    var isDebug = true

    private const val TAG = "XposedLogger"

    private const val LOG_FILE_NAME = "twig.log"

    private const val MAX_LOG_SIZE = 512 * 1024L

    private val lock = Any()

    private val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    private var packageName: String = ""

    @Volatile
    private var logFile: File? = null

    @Volatile
    private var onlyError: Boolean = false

    /**
     * 在目标应用进程启动时调用，初始化日志文件。
     */
    fun init(packageName: String, debug: Boolean = true, onlyError: Boolean = false) {
        this.packageName = packageName
        this.isDebug = debug
        this.onlyError = onlyError
        this.logFile = runCatching {
            Environment.getExternalStorageDirectory()
                .resolve("Android/media/$packageName")
                .also { if (!it.exists()) it.mkdirs() }
                .resolve(LOG_FILE_NAME)
        }.getOrNull()
    }

    fun load(): List<String> = readLog()

    fun readLog(): List<String> {
        val file = logFile ?: return emptyList()
        return runCatching {
            if (file.exists()) file.readLines() else emptyList()
        }.getOrDefault(emptyList())
    }

    fun clear() {
        val file = logFile ?: return
        runCatching { if (file.exists()) file.delete() }
    }

    fun i(message: String) {
        Log.i(TAG, message)
        log('I', message)
    }

    fun d(message: String) {
        if (!isDebug) return
        Log.d(TAG, message)
        log('D', message)
    }

    fun w(message: String) {
        Log.w(TAG, message)
        log('W', message)
    }

    fun e(string: String) {
        Log.e(TAG, string)
        log('E', string)
    }

    fun e(throwable: Throwable) {
        val stackTrace = throwable.stackTraceToString()
        Log.e(TAG, stackTrace)
        log('E', stackTrace)
    }

    private fun log(level: Char, message: String) {
        if (onlyError && level != 'E') return
        val file = logFile ?: return

        synchronized(lock) {
            val line = runCatching {
                "${timeFormat.format(Date())} $level/${packageName.ifBlank { "unknown" }}: $message"
            }.getOrElse { "$level/${packageName.ifBlank { "unknown" }}: $message" }

            runCatching {
                val parent = file.parentFile ?: return@runCatching
                if (!parent.exists()) parent.mkdirs()
                if (file.exists() && file.length() > MAX_LOG_SIZE) {
                    val backup = File(parent, "$LOG_FILE_NAME.1")
                    runCatching { if (backup.exists()) backup.delete() }
                    file.renameTo(backup)
                }
                file.appendText(line + "\n")
            }
        }
    }
}
