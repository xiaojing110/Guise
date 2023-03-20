package com.houvven.twig.ui

import java.util.Locale

enum class LanguageSupport(locale: Locale) {
    CHINESE(Locale.CHINESE),
    ENGLISH(Locale.ENGLISH)
    ;

    val label: String = locale.displayName

    val value = locale.toString()
}