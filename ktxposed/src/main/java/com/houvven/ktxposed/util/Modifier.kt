package com.houvven.ktxposed.util

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

val Method.isStatic: Boolean get() = Modifier.isStatic(modifiers)

val Method.isPublic: Boolean get() = Modifier.isPublic(modifiers)

val Method.isPrivate: Boolean get() = Modifier.isPrivate(modifiers)

val Method.isProtected: Boolean get() = Modifier.isProtected(modifiers)

val Method.isFinal: Boolean get() = Modifier.isFinal(modifiers)

val Method.isAbstract: Boolean get() = Modifier.isAbstract(modifiers)

val Method.isNative: Boolean get() = Modifier.isNative(modifiers)


val Class<*>.isStatic: Boolean get() = Modifier.isStatic(modifiers)

val Class<*>.isPublic: Boolean get() = Modifier.isPublic(modifiers)

val Class<*>.isPrivate: Boolean get() = Modifier.isPrivate(modifiers)

val Class<*>.isProtected: Boolean get() = Modifier.isProtected(modifiers)

val Class<*>.isFinal: Boolean get() = Modifier.isFinal(modifiers)

val Class<*>.isAbstract: Boolean get() = Modifier.isAbstract(modifiers)


val Field.isStatic: Boolean get() = Modifier.isStatic(modifiers)

val Field.isPublic: Boolean get() = Modifier.isPublic(modifiers)

val Field.isPrivate: Boolean get() = Modifier.isPrivate(modifiers)

val Field.isProtected: Boolean get() = Modifier.isProtected(modifiers)

val Field.isFinal: Boolean get() = Modifier.isFinal(modifiers)

val Field.isAbstract: Boolean get() = Modifier.isAbstract(modifiers)


val Constructor<*>.isPublic: Boolean get() = Modifier.isPublic(modifiers)

val Constructor<*>.isPrivate: Boolean get() = Modifier.isPrivate(modifiers)

val Constructor<*>.isProtected: Boolean get() = Modifier.isProtected(modifiers)
