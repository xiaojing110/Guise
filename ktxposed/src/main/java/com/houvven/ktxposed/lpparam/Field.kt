@file:Suppress("unused")

package com.houvven.ktxposed.lpparam

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





