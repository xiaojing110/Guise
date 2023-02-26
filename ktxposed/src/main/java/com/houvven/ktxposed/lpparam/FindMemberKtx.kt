@file:Suppress("unused", "MemberVisibilityCanBePrivate")


package com.houvven.ktxposed.lpparam

import com.houvven.ktxposed.LocalHookParam
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * Find a [Class] by given class name and [ClassLoader].
 * If the class is not found, [XposedHelpers.ClassNotFoundError] will be thrown.
 * @param className Class name
 * @param classLoader Class loader
 * @return [Class]
 * @throws XposedHelpers.ClassNotFoundError
 * @see XposedHelpers.findClass
 */
@Throws(XposedHelpers.ClassNotFoundError::class)
fun findClass(
    className: String,
    classLoader: ClassLoader = LocalHookParam.lpparam.classLoader
): Class<*> = XposedHelpers.findClass(className, classLoader)


/**
 * @see findClass(String, ClassLoader)
 * @throws XposedHelpers.ClassNotFoundError
 */
@Throws(XposedHelpers.ClassNotFoundError::class)
inline fun findClass(
    className: String,
    classLoader: ClassLoader = LocalHookParam.lpparam.classLoader,
    block: Class<*>.() -> Unit
) = findClass(className, classLoader).block()


/**
 * Find a [Class] by given class name and [ClassLoader].
 * If the class is not found, null will be returned.
 *
 * @param className
 * @param classLoader
 * @return [Class] or Null
 * @see XposedHelpers.findClassIfExists
 */
fun findClassOrNull(
    className: String,
    classLoader: ClassLoader = LocalHookParam.lpparam.classLoader
): Class<*>? = XposedHelpers.findClassIfExists(className, classLoader)


/**
 * @see findClassOrNull(String, ClassLoader)
 */
inline fun findClassOrNull(
    className: String,
    classLoader: ClassLoader = LocalHookParam.lpparam.classLoader,
    block: Class<*>.() -> Unit
) = findClassOrNull(className, classLoader)?.block()


/**
 * Find a best match method by given method name and parameter types.
 * If the method is not found, [NoSuchMethodError] will be thrown.
 *
 * @param methodName
 * @param parameterTypes
 * @return [Method]
 * @throws NoSuchMethodError
 * @see XposedHelpers.findMethodBestMatch
 */
@Throws(NoSuchMethodError::class)
fun Class<*>.findMethodBestMatch(
    methodName: String,
    vararg parameterTypes: Class<*>
): Method = XposedHelpers.findMethodBestMatch(this, methodName, *parameterTypes)


/**
 * @see Class.findMethodBestMatch(String, Class<*>...)
 * @throws NoSuchMethodError
 */
@Throws(NoSuchMethodError::class)
inline fun Class<*>.findMethodBestMatch(
    methodName: String,
    vararg parameterTypes: Class<*>,
    block: Method.() -> Unit
) = findMethodBestMatch(methodName, *parameterTypes).block()


/**
 * Find method best match
 *
 * @param methodName
 * @param parameterTypes
 * @return [Method]
 * @see Class.findMethodBestMatch(String, Class<*>...)
 */
@Throws(NoSuchMethodError::class)
fun KClass<*>.findMethodBestMatch(
    methodName: String,
    vararg parameterTypes: KClass<*>
): Method =
    this.java.findMethodBestMatch(methodName, *parameterTypes.map { it.java }.toTypedArray())


/**
 * @see KClass.findMethodBestMatch(String, KClass<*>...)
 * @throws NoSuchMethodError
 */
@Throws(NoSuchMethodError::class)
inline fun KClass<*>.findMethodBestMatch(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    block: Method.() -> Unit
) = findMethodBestMatch(methodName, *parameterTypes).block()


/**
 * Find a method by given method name and parameter types in given class.
 * If the method is not found, [NoSuchMethodError] will be thrown.
 *
 * @param methodName
 * @param parameterTypes
 * @return [Method]
 * @throws NoSuchMethodError
 * @see XposedHelpers.findMethodExact
 */
@Throws(NoSuchMethodError::class)
fun Class<*>.findMethodExact(methodName: String, vararg parameterTypes: Class<*>): Method =
    XposedHelpers.findMethodExact(this, methodName, *parameterTypes)


/**
 * @see Class.findMethodExact(String, Class<*>...)
 * @throws NoSuchMethodError
 */
@Throws(NoSuchMethodError::class)
inline fun Class<*>.findMethodExact(
    methodName: String,
    vararg parameterTypes: Class<*>,
    block: Method.() -> Unit
) = findMethodExact(methodName, *parameterTypes).block()


/**
 * Find method exact
 *
 * @param methodName
 * @param parameterTypes
 * @return [Method]
 * @throws NoSuchMethodError
 * @see Class.findMethodExact(String, Class<*>...)
 */
@Throws(NoSuchMethodError::class)
fun KClass<*>.findMethodExact(methodName: String, vararg parameterTypes: KClass<*>): Method =
    this.java.findMethodExact(methodName, *parameterTypes.map { it.java }.toTypedArray())


/**
 * @see KClass.findMethodExact(String, KClass<*>...)
 * @throws NoSuchMethodError
 */
@Throws(NoSuchMethodError::class)
inline fun KClass<*>.findMethodExact(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    block: Method.() -> Unit
) = findMethodExact(methodName, *parameterTypes).block()


/**
 * Find a method by given method name and parameter types in given class.
 * If the method is not found, null will be returned.
 *
 * @param methodName
 * @param parameterTypes
 * @return [Method] or Null
 * @see XposedHelpers.findMethodExactIfExists
 */
fun Class<*>.findMethodExactOrNull(methodName: String, vararg parameterTypes: Class<*>): Method? =
    XposedHelpers.findMethodExactIfExists(this, methodName, *parameterTypes)


/**
 * @see Class.findMethodExactOrNull(String, Class<*>...)
 */
inline fun Class<*>.findMethodExactOrNull(
    methodName: String,
    vararg parameterTypes: Class<*>,
    block: Method.() -> Unit
) = findMethodExactOrNull(methodName, *parameterTypes)?.block()


/**
 * Find method exact or null
 *
 * @param methodName
 * @param parameterTypes
 * @return [Method] or Null
 * @see Class.findMethodExactOrNull(String, Class<*>...)
 */
fun KClass<*>.findMethodExactOrNull(methodName: String, vararg parameterTypes: KClass<*>): Method? =
    this.java.findMethodExactOrNull(methodName, *parameterTypes.map { it.java }.toTypedArray())


/**
 * @see KClass.findMethodExactOrNull(String, KClass<*>...)
 */
inline fun KClass<*>.findMethodExactOrNull(
    methodName: String,
    vararg parameterTypes: KClass<*>,
    block: Method.() -> Unit
) = findMethodExactOrNull(methodName, *parameterTypes)?.block()


/**
 * Find something methods by given method name and parameter types in given class.
 *
 * @param returnType
 * @param parameterTypes
 * @return [Array]
 * @see XposedHelpers.findMethodsByExactParameters
 */
fun Class<*>.findMethodsByExactParameters(
    returnType: Class<*>,
    vararg parameterTypes: Class<*>
): Array<Method> = XposedHelpers.findMethodsByExactParameters(this, returnType, *parameterTypes)


/**
 * @see Class.findMethodsByExactParameters(Class<*>, Class<*>...)
 */
inline fun Class<*>.findMethodsByExactParameters(
    returnType: Class<*>,
    vararg parameterTypes: Class<*>,
    block: Array<Method>.() -> Unit
) = findMethodsByExactParameters(returnType, *parameterTypes).block()


/**
 * @see Class.findMethodsByExactParameters(Class<*>, Class<*>...)
 */
fun KClass<*>.findMethodsByExactParameters(
    returnType: KClass<*>,
    vararg parameterTypes: KClass<*>
): Array<Method> = this.java.findMethodsByExactParameters(
    returnType.java,
    *parameterTypes.map { it.java }.toTypedArray()
)


/**
 * @see Class.findMethodsByExactParameters(Class<*>, Class<*>...)
 */
inline fun KClass<*>.findMethodsByExactParameters(
    returnType: KClass<*>,
    vararg parameterTypes: KClass<*>,
    block: Array<Method>.() -> Unit
) = findMethodsByExactParameters(returnType, *parameterTypes).block()


/**
 * Find a field by given field name in given class.
 * If the field is not found, [NoSuchFieldError] will be thrown.
 *
 * @param fieldName
 * @return [Field]
 * @throws NoSuchFieldError
 * @see XposedHelpers.findField
 */
@Throws(NoSuchFieldError::class)
fun Class<*>.findField(fieldName: String): Field = XposedHelpers.findField(this, fieldName)


/**
 * @see Class.findField(String)
 * @throws NoSuchFieldError
 */
@Throws(NoSuchFieldError::class)
inline fun Class<*>.findField(fieldName: String, block: Field.() -> Unit) = findField(fieldName).block()


/**
 * @see Class.findField(String)
 * @throws NoSuchFieldError
 */
@Throws(NoSuchFieldError::class)
fun KClass<*>.findField(fieldName: String): Field = this.java.findField(fieldName)


/**
 * @see Class.findField(String)
 * @throws NoSuchFieldError
 */
@Throws(NoSuchFieldError::class)
inline fun KClass<*>.findField(fieldName: String, block: Field.() -> Unit) = findField(fieldName).block()


/**
 * Find a field by given field name in given class.
 * If the field is not found, null will be returned.
 *
 * @param fieldName
 * @return [Field] or Null
 * @see XposedHelpers.findFieldIfExists
 */
fun Class<*>.findFieldOrNull(fieldName: String): Field? =
    XposedHelpers.findFieldIfExists(this, fieldName)


/**
 * @see Class.findFieldOrNull(String)
 */
inline fun Class<*>.findFieldOrNull(fieldName: String, block: Field.() -> Unit) =
    findFieldOrNull(fieldName)?.block()


/**
 * @see Class.findFieldOrNull(String)
 */
fun KClass<*>.findFieldOrNull(fieldName: String): Field? = this.java.findFieldOrNull(fieldName)


/**
 * @see Class.findFieldOrNull(String)
 */
inline fun KClass<*>.findFieldOrNull(fieldName: String, block: Field.() -> Unit) =
    findFieldOrNull(fieldName)?.block()


/**
 * Find first field by exact type in given class.
 * If the field is not found, [NoSuchFieldError] will be thrown.
 *
 * @param fieldType
 * @return [Field]
 * @throws NoSuchFieldError
 * @see XposedHelpers.findFirstFieldByExactType
 */
@Throws(NoSuchFieldError::class)
fun Class<*>.findFirstFieldByExactType(fieldType: Class<*>): Field =
    XposedHelpers.findFirstFieldByExactType(this, fieldType)


/**
 * @see Class.findFirstFieldByExactType(Class<*>)
 * @throws NoSuchFieldError
 */
@Throws(NoSuchFieldError::class)
inline fun Class<*>.findFirstFieldByExactType(fieldType: Class<*>, block: Field.() -> Unit) =
    findFirstFieldByExactType(fieldType).block()


/**
 * @see Class.findFirstFieldByExactType(Class<*>)
 * @throws NoSuchFieldError
 */
@Throws(NoSuchFieldError::class)
fun KClass<*>.findFirstFieldByExactType(fieldType: KClass<*>): Field =
    this.java.findFirstFieldByExactType(fieldType.java)


/**
 * @see Class.findFirstFieldByExactType(Class<*>)
 * @throws NoSuchFieldError
 */
@Throws(NoSuchFieldError::class)
inline fun KClass<*>.findFirstFieldByExactType(fieldType: KClass<*>, block: Field.() -> Unit) =
    findFirstFieldByExactType(fieldType).block()

