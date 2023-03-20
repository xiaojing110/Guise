package com.houvven.twig.preset

import com.houvven.twig.preset.adapter.PresetAdapter

@Suppress("unused", "SpellCheckingInspection")
enum class CharacteristicsPreset(
    override val label: String,
    override val value: String
) : PresetAdapter {
    UNHOOK("Unhook", ""),
    TABLET("tablet", "tablet"),
    NOSDCARD("nosdcard", "nosdcard"),
    DEFAULT("default", "default")
}