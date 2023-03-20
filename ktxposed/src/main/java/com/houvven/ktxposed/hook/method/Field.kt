@file:Suppress("unused")

package com.houvven.ktxposed.hook.method

import com.houvven.ktxposed.util.runXposedCatching
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getStaticBooleanField
import de.robv.android.xposed.XposedHelpers.getStaticByteField
import de.robv.android.xposed.XposedHelpers.getStaticCharField
import de.robv.android.xposed.XposedHelpers.getStaticDoubleField
import de.robv.android.xposed.XposedHelpers.getStaticFloatField
import de.robv.android.xposed.XposedHelpers.getStaticIntField
import de.robv.android.xposed.XposedHelpers.getStaticLongField
import de.robv.android.xposed.XposedHelpers.getStaticObjectField
import de.robv.android.xposed.XposedHelpers.getStaticShortField
import de.robv.android.xposed.XposedHelpers.setStaticBooleanField
import de.robv.android.xposed.XposedHelpers.setStaticByteField
import de.robv.android.xposed.XposedHelpers.setStaticCharField
import de.robv.android.xposed.XposedHelpers.setStaticDoubleField
import de.robv.android.xposed.XposedHelpers.setStaticFloatField
import de.robv.android.xposed.XposedHelpers.setStaticIntField
import de.robv.android.xposed.XposedHelpers.setStaticLongField
import de.robv.android.xposed.XposedHelpers.setStaticObjectField
import de.robv.android.xposed.XposedHelpers.setStaticShortField
import kotlin.reflect.KClass


@Throws(NoSuchFieldError::class)
inline fun <reified T> Class<*>.setStaticField(name: String, value: T) {
    when (T::class.java) {
        Boolean::class.java -> setStaticBooleanField(this, name, value as Boolean)
        Byte::class.java -> setStaticByteField(this, name, value as Byte)
        Char::class.java -> setStaticCharField(this, name, value as Char)
        Short::class.java -> setStaticShortField(this, name, value as Short)
        Int::class.java -> setStaticIntField(this, name, value as Int)
        Long::class.java -> setStaticLongField(this, name, value as Long)
        Float::class.java -> setStaticFloatField(this, name, value as Float)
        Double::class.java -> setStaticDoubleField(this, name, value as Double)
        else -> setStaticObjectField(this, name, value)
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
        Boolean::class.java -> getStaticBooleanField(this, name) as T
        Byte::class.java -> getStaticByteField(this, name) as T
        Char::class.java -> getStaticCharField(this, name) as T
        Short::class.java -> getStaticShortField(this, name) as T
        Int::class.java -> getStaticIntField(this, name) as T
        Long::class.java -> getStaticLongField(this, name) as T
        Float::class.java -> getStaticFloatField(this, name) as T
        Double::class.java -> getStaticDoubleField(this, name) as T
        else -> getStaticObjectField(this, name) as T
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


