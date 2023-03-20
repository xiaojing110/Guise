package com.houvven.twig.ui.utils

import androidx.compose.ui.platform.UriHandler

fun toMail2(address: String) = "mailto:$address"

infix fun UriHandler.mailto(address: String) = openUri(toMail2(address))