package com.houvven.twig.xposed

import com.houvven.twig.TwigStore
import com.houvven.twig.config.TwigConfig

internal object LocalHookConfig {
    lateinit var current: TwigConfig.Config
        private set
    fun updateValue() {
        current = TwigStore.XPOSED.get()
    }
}