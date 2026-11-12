package com.houvven.ktxposed.compat

/**
 * 兼容旧 Xposed API 的优先级常量。
 *
 * 这些常量只是为了保持旧 Hook 代码不用整体改写，实际 Hook 由
 * LSPosed API 102 的 [io.github.libxposed.api.XposedInterface] 完成。
 */
object XCallback {
    const val PRIORITY_DEFAULT: Int = 0
    const val PRIORITY_LOWEST: Int = -10000
    const val PRIORITY_HIGHEST: Int = 10000
}
