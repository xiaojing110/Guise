@file:Suppress("unused")

package com.houvven.androidc.ktx


inline infix fun Boolean.ifTrue(callback: () -> Unit) {
    if (this) callback()
}


inline infix fun Boolean.ifFalse(callback: () -> Unit) {
    if (!this) callback()
}