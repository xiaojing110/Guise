package com.houvven.twig.preset

import android.os.Build
import com.houvven.twig.preset.adapter.PresetAdapter
import com.houvven.twig.preset.device.DevicePreset
import com.houvven.ktxposed.util.isStatic

/**
 * 所有预设选项
 */
@Suppress("unused")
object Presets {
    val MANUFACTURER by lazy { DevicePreset.brandsPreset }

    val RELEASE: List<PresetAdapter> by lazy { ReleasePreset.values().toList().reversed() }

    val SDK_INT: List<PresetAdapter> by lazy {
        Build.VERSION_CODES::class.java.let { clazz ->
            clazz.fields
                .filter { it.isStatic && it.name != "CUR_DEVELOPMENT" }
                .map {
                    object : PresetAdapter {
                        override val label: String = it.name
                        override val value: String = it.getInt(clazz).toString()
                    }
                }
                .sortedBy { it.value.toInt() }
                .reversed()
        }
    }

    val LANGUAGE: List<PresetAdapter> by lazy { LanguagePreset.values().toList() }

    val TIMEZONE: List<PresetAdapter> by lazy { TimeZonePreset.values().toList() }

    val NETWORK_TYPE: List<PresetAdapter> by lazy { NetworkType.values().toList() }

    val MOBILE_NETWORK_TYPE: List<PresetAdapter> by lazy { MobileNetworkType.values().toList() }

    val TEL_NET_TYPE: List<PresetAdapter> by lazy { MobileNetworkType.values().toList() }

    val SIM: List<PresetAdapter> by lazy { SimPreset.values().toList() }

    val CHARACTERISTICS: List<PresetAdapter> by lazy { CharacteristicsPreset.values().toList() }

    val USB_DEBUGGING: List<PresetAdapter> by lazy { UsbDebuggingPreset.values().toList() }

    val SCREENSHOTS: List<PresetAdapter> by lazy { ScreenshotsPreset.values().toList() }
}