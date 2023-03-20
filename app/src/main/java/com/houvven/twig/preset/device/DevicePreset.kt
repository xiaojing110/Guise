package com.houvven.twig.preset.device

import com.houvven.twig.ContextAmbient
import com.houvven.twig.preset.adapter.PresetAdapter

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DevicePreset {

    val allBrands = DeviceDBHelper(ContextAmbient.current).use { dbHelper ->
        dbHelper.getAllBrand()
    }

    val brandsPreset: List<PresetAdapter> by lazy {
        allBrands.map {
            object : PresetAdapter {
                override val label: String = it.value
                override val value: String = it.key
            }
        }
    }

    fun getModelsPreset(brand: String): List<PresetAdapter> {
        return DeviceDBHelper(ContextAmbient.current).use { dbHelper ->
            dbHelper.getDevicesByBrand(brand)
                .filterNot { it.modelName.isNullOrBlank() || it.model.isNullOrBlank() }
                .map {
                    val label =
                        if (it.verName == "#" || it.verName == null) it.modelName!!
                        else "${it.modelName!!} (${it.verName.removePrefix("#")})"
                    object : PresetAdapter {
                        override val label: String = label
                        override val value: String = "${it.model!!}:${it.codeAlias ?: ""}"
                    }
                }
        }
    }

}