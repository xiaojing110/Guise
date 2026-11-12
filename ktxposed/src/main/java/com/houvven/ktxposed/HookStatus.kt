package com.houvven.ktxposed

import android.os.Environment
import java.io.File

/**
 * HookStatus
 * @author houvven
 * @since 2023/2/23
 * @version 1.0
 */
@Suppress("unused")
class HookStatus {
    companion object {
        private fun isHooked(): Boolean = runCatching {
            val media = Environment.getExternalStorageDirectory().resolve("Android/media")
            media.listFiles()?.any { dir ->
                dir.isDirectory &&
                        (File(dir, "twig_active").exists() || File(dir, "twig.log").exists())
            } == true
        }.getOrDefault(false)

        @get:JvmName("isModuleActivated")
        val isHooked: Boolean get() = isHooked()
    }
}
