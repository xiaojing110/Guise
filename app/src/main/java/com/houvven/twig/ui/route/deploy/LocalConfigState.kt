package com.houvven.twig.ui.route.deploy

import androidx.compose.runtime.mutableStateOf
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.preset.adapter.PresetAdapter

object LocalConfigState {
    internal val setValueMethod = mutableStateOf({ _: String -> })
    internal val preset = mutableStateOf(emptyList<PresetAdapter>())
    internal val currentStateCache = mutableStateOf(TwigConfig.State.of(TwigConfig.Config()))
    internal var isBackFromMapPointSelection = false

    internal fun setValue(value: String) {
        setValueMethod.value(value)
    }
}