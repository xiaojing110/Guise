package com.houvven.twig.preset

import com.houvven.twig.preset.adapter.PresetAdapter
import com.houvven.twig.xposed.HookValues


enum class UsbDebuggingPreset(
    override val label: String,
    override val value: String
) : PresetAdapter {
    UNHOOK("Unhook", ""),
    ENABLED("enabled", HookValues.USB_DEBUGGING_ENABLE.toString()),
    DISABLED("disabled", HookValues.USB_DEBUGGING_DISABLE.toString())
}


enum class ScreenshotsPreset(
    override val label: String,
    override val value: String
) : PresetAdapter {
    UNHOOK("Unhook", ""),
    ENABLED("Force enabled", HookValues.SCREEN_SHOTS_ENABLE.toString()),
    DISABLED("Force disabled", HookValues.SCREEN_SHOTS_DISABLE.toString())
}