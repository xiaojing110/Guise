package com.houvven.twig.xposed.adapter

import com.houvven.ktxposed.hook.method.service.HookLoadPackage
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.xposed.LocalHookConfig

interface TwigHookAdapter : HookLoadPackage {
    val config get() = LocalHookConfig.current

    val default: TwigConfig.Config get() = TwigConfig.Config.default
}