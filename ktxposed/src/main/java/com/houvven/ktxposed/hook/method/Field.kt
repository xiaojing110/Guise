@file:Suppress("unused")

package com.houvven.ktxposed.hook.method

import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.ktxposed.compat.XposedHelpers
import kotlin.reflect.KClass


@Throws(NoSuchFieldError::class)
inline fun <reified T> Class<*>.setStaticField(name: String, value: T) {
    when (T::class.java) {
        Boolean::class.java -> XposedHelpers.setStaticBooleanField(this, name, value as Boolean)
        Byte::class.java -> XposedHelpers.setStaticByteField(this, name, value as Byte)
        Char::class.java -> XposedHelpers.setStaticCharField(this, name, value as Char)
        Short::class.java -> XposedHelpers.setStaticShortField(this, name, value as Short)
        Int::class.java -> XposedHelpers.setStaticIntField(this, name, value as Int)
        Long::class.java -> XposedHelpers.setStaticLongField(this, name, value as Long)
        Float::class.java -> XposedHelpers.setStaticFloatField(this, name, value as Float)
        Double::class.java -> XposedHelpers.setStaticDoubleField(this, name, value as Double)
        else -> XposedHelpers.setStaticObjectField(this, name, value)
    }
}


@Throws(NoSuchFieldError::class)
inline fun <reified T> KClass<*>.setStaticField(name: String, value: T) {
    java.setStaticField(name, value)
}

inline fun <reified T> Class<*>.setStaticFieldIfExits(name: String, value: T) {
    findFieldOrNull(name)?.let {
        setStaticField(name, value)
    }
}

inline fun <reified T> KClass<*>.setStaticFieldIfExits(name: String, value: T) {
    java.setStaticField(name, value)
}

@Throws(NoSuchFieldError::class)
inline fun <reified T> Class<*>.getStaticField(name: String): T {
    return when (T::class.java) {
        Boolean::class.java -> XposedHelpers.getStaticBooleanField(this, name) as T
        Byte::class.java -> XposedHelpers.getStaticByteField(this, name) as T
        Char::class.java -> XposedHelpers.getStaticCharField(this, name) as T
        Short::class.java -> XposedHelpers.getStaticShortField(this, name) as T
        Int::class.java -> XposedHelpers.getStaticIntField(this, name) as T
        Long::class.java -> XposedHelpers.getStaticLongField(this, name) as T
        Float::class.java -> XposedHelpers.getStaticFloatField(this, name) as T
        Double::class.java -> XposedHelpers.getStaticDoubleField(this, name) as T
        else -> XposedHelpers.getStaticObjectField(this, name) as T
    }
}


@Throws(NoSuchFieldError::class)
inline fun <reified T> KClass<*>.getStaticField(name: String): T {
    return java.getStaticField(name)
}


inline fun <reified T> Class<*>.getStaticFieldOrNull(name: String): T? {
    return try {
        getStaticField(name)
    } catch (e: NoSuchFieldException) {
        null
    }
}


inline fun <reified T> KClass<*>.getStaticFieldOrNull(name: String): T? {
    return java.getStaticFieldOrNull(name)
}


@Throws(NoSuchFieldError::class)
inline fun <reified T> Any.setInstanceField(name: String, value: T) {
    when (T::class.java) {
        Boolean::class.java -> XposedHelpers.setBooleanField(this, name, value as Boolean)
        Byte::class.java -> XposedHelpers.setByteField(this, name, value as Byte)
        Char::class.java -> XposedHelpers.setCharField(this, name, value as Char)
        Short::class.java -> XposedHelpers.setShortField(this, name, value as Short)
        Int::class.java -> XposedHelpers.setIntField(this, name, value as Int)
        Long::class.java -> XposedHelpers.setLongField(this, name, value as Long)
        Float::class.java -> XposedHelpers.setFloatField(this, name, value as Float)
        Double::class.java -> XposedHelpers.setDoubleField(this, name, value as Double)
        else -> XposedHelpers.setObjectField(this, name, value)
    }
}


@Throws(NoSuchFieldError::class)
inline fun <reified T> Any.getInstanceField(name: String): T {
    return when (T::class.java) {
        Boolean::class.java -> XposedHelpers.getBooleanField(this, name) as T
        Byte::class.java -> XposedHelpers.getByteField(this, name) as T
        Char::class.java -> XposedHelpers.getCharField(this, name) as T
        Short::class.java -> XposedHelpers.getShortField(this, name) as T
        Int::class.java -> XposedHelpers.getIntField(this, name) as T
        Long::class.java -> XposedHelpers.getLongField(this, name) as T
        Float::class.java -> XposedHelpers.getFloatField(this, name) as T
        Double::class.java -> XposedHelpers.getDoubleField(this, name) as T
        else -> XposedHelpers.getObjectField(this, name) as T
    }
}


inline fun <reified T> Any.getInstanceFieldOrNull(name: String): T? {
    return try {
        getInstanceField(name)
    } catch (e: NoSuchFieldException) {
        null
    }
}


