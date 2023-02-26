package com.houvven.ktxposed

/**
 * HookStatus
 * @author houvven
 * @since 2023/2/23
 * @version 1.0
 */
@Suppress("unused")
class HookStatus {
    companion object {
        private fun isHooked(): Boolean {
            return false
        }

        @get:JvmName("isModuleActivated")
        val isHooked: Boolean = isHooked()
    }
}