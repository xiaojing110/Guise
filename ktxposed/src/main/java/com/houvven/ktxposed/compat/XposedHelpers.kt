package com.houvven.ktxposed.compat

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 旧 XposedHelpers 的最小反射实现。
 *
 * 只保留本项目实际使用的查询、字段读写和方法调用能力，不依赖旧 Xposed
 * 运行库，因此可以完全编译在 LSPosed API 102 之上。
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object XposedHelpers {

    class ClassNotFoundError internal constructor(
        className: String,
        cause: Throwable? = null
    ) : NoClassDefFoundError(className) {
        init {
            if (cause != null) initCause(cause)
        }
    }

    @JvmStatic
    fun findClass(className: String, classLoader: ClassLoader? = null): Class<*> {
        return try {
            Class.forName(className, false, classLoader ?: XposedBridge.BOOTCLASSLOADER)
        } catch (e: ClassNotFoundException) {
            throw ClassNotFoundError(className, e)
        }
    }

    @JvmStatic
    fun findClassIfExists(className: String, classLoader: ClassLoader? = null): Class<*>? =
        try {
            Class.forName(className, false, classLoader ?: XposedBridge.BOOTCLASSLOADER)
        } catch (e: Throwable) {
            null
        }

    @JvmStatic
    fun findMethodExact(
        clazz: Class<*>,
        methodName: String,
        vararg parameterTypes: Class<*>
    ): Method = findMethodExactIfExists(clazz, methodName, *parameterTypes)
        ?: throw NoSuchMethodError("$clazz#$methodName")

    @JvmStatic
    fun findMethodExactIfExists(
        clazz: Class<*>,
        methodName: String,
        vararg parameterTypes: Class<*>
    ): Method? = methodsOf(clazz).firstOrNull { method ->
        method.name == methodName &&
                method.parameterTypes.size == parameterTypes.size &&
                method.parameterTypes.contentEquals(parameterTypes)
    }

    @JvmStatic
    fun findMethodBestMatch(
        clazz: Class<*>,
        methodName: String,
        vararg parameterTypes: Class<*>
    ): Method {
        findMethodExactIfExists(clazz, methodName, *parameterTypes)?.let { return it }

        return methodsOf(clazz).firstOrNull { method ->
            method.name == methodName &&
                    method.parameterTypes.size == parameterTypes.size &&
                    method.parameterTypes.indices.all {
                        isAssignable(method.parameterTypes[it], parameterTypes[it])
                    }
        } ?: throw NoSuchMethodError("$clazz#$methodName")
    }

    @JvmStatic
    fun findMethodsByExactParameters(
        clazz: Class<*>,
        returnType: Class<*>,
        vararg parameterTypes: Class<*>
    ): Array<Method> = methodsOf(clazz).filter { method ->
        method.returnType == returnType &&
                method.parameterTypes.size == parameterTypes.size &&
                method.parameterTypes.contentEquals(parameterTypes)
    }.toList().toTypedArray()

    @JvmStatic
    fun findField(clazz: Class<*>, fieldName: String): Field =
        findFieldIfExists(clazz, fieldName) ?: throw NoSuchFieldError("$clazz#$fieldName")

    @JvmStatic
    fun findFieldIfExists(clazz: Class<*>, fieldName: String): Field? =
        classesOf(clazz)
            .flatMap { it.declaredFields.asSequence() }
            .firstOrNull { it.name == fieldName }

    @JvmStatic
    fun findFirstFieldByExactType(clazz: Class<*>, fieldType: Class<*>): Field =
        classesOf(clazz)
            .flatMap { it.declaredFields.asSequence() }
            .firstOrNull { it.type == fieldType }
            ?: throw NoSuchFieldError("$clazz field of type $fieldType")

    @JvmStatic
    fun findConstructorExact(clazz: Class<*>, vararg parameterTypes: Class<*>): Constructor<*> =
        clazz.declaredConstructors.firstOrNull { constructor ->
            constructor.parameterTypes.size == parameterTypes.size &&
                    constructor.parameterTypes.contentEquals(parameterTypes)
        } ?: throw NoSuchMethodError("$clazz constructor")

    @JvmStatic
    fun findConstructorBestMatch(
        clazz: Class<*>,
        vararg parameterTypes: Class<*>
    ): Constructor<*> {
        clazz.declaredConstructors.firstOrNull { constructor ->
            constructor.parameterTypes.size == parameterTypes.size &&
                    constructor.parameterTypes.indices.all {
                        isAssignable(constructor.parameterTypes[it], parameterTypes[it])
                    }
        }?.let { return it }
        throw NoSuchMethodError("$clazz constructor")
    }

    @JvmStatic
    fun getStaticObjectField(clazz: Class<*>, fieldName: String): Any? =
        field(clazz, fieldName).get(null)

    @JvmStatic
    fun getStaticBooleanField(clazz: Class<*>, fieldName: String): Boolean =
        getStaticObjectField(clazz, fieldName) as Boolean

    @JvmStatic
    fun getStaticByteField(clazz: Class<*>, fieldName: String): Byte =
        getStaticObjectField(clazz, fieldName) as Byte

    @JvmStatic
    fun getStaticCharField(clazz: Class<*>, fieldName: String): Char =
        getStaticObjectField(clazz, fieldName) as Char

    @JvmStatic
    fun getStaticShortField(clazz: Class<*>, fieldName: String): Short =
        getStaticObjectField(clazz, fieldName) as Short

    @JvmStatic
    fun getStaticIntField(clazz: Class<*>, fieldName: String): Int =
        getStaticObjectField(clazz, fieldName) as Int

    @JvmStatic
    fun getStaticLongField(clazz: Class<*>, fieldName: String): Long =
        getStaticObjectField(clazz, fieldName) as Long

    @JvmStatic
    fun getStaticFloatField(clazz: Class<*>, fieldName: String): Float =
        getStaticObjectField(clazz, fieldName) as Float

    @JvmStatic
    fun getStaticDoubleField(clazz: Class<*>, fieldName: String): Double =
        getStaticObjectField(clazz, fieldName) as Double

    @JvmStatic
    fun setStaticObjectField(clazz: Class<*>, fieldName: String, value: Any?) {
        field(clazz, fieldName).set(null, value)
    }

    @JvmStatic
    fun setStaticBooleanField(clazz: Class<*>, fieldName: String, value: Boolean) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticByteField(clazz: Class<*>, fieldName: String, value: Byte) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticCharField(clazz: Class<*>, fieldName: String, value: Char) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticShortField(clazz: Class<*>, fieldName: String, value: Short) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticIntField(clazz: Class<*>, fieldName: String, value: Int) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticLongField(clazz: Class<*>, fieldName: String, value: Long) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticFloatField(clazz: Class<*>, fieldName: String, value: Float) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun setStaticDoubleField(clazz: Class<*>, fieldName: String, value: Double) =
        setStaticObjectField(clazz, fieldName, value)

    @JvmStatic
    fun getObjectField(obj: Any, fieldName: String): Any? =
        field(obj.javaClass, fieldName).get(obj)

    @JvmStatic
    fun getBooleanField(obj: Any, fieldName: String): Boolean =
        getObjectField(obj, fieldName) as Boolean

    @JvmStatic
    fun getByteField(obj: Any, fieldName: String): Byte =
        getObjectField(obj, fieldName) as Byte

    @JvmStatic
    fun getCharField(obj: Any, fieldName: String): Char =
        getObjectField(obj, fieldName) as Char

    @JvmStatic
    fun getShortField(obj: Any, fieldName: String): Short =
        getObjectField(obj, fieldName) as Short

    @JvmStatic
    fun getIntField(obj: Any, fieldName: String): Int =
        getObjectField(obj, fieldName) as Int

    @JvmStatic
    fun getLongField(obj: Any, fieldName: String): Long =
        getObjectField(obj, fieldName) as Long

    @JvmStatic
    fun getFloatField(obj: Any, fieldName: String): Float =
        getObjectField(obj, fieldName) as Float

    @JvmStatic
    fun getDoubleField(obj: Any, fieldName: String): Double =
        getObjectField(obj, fieldName) as Double

    @JvmStatic
    fun setObjectField(obj: Any, fieldName: String, value: Any?) {
        field(obj.javaClass, fieldName).set(obj, value)
    }

    @JvmStatic
    fun setBooleanField(obj: Any, fieldName: String, value: Boolean) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setByteField(obj: Any, fieldName: String, value: Byte) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setCharField(obj: Any, fieldName: String, value: Char) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setShortField(obj: Any, fieldName: String, value: Short) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setIntField(obj: Any, fieldName: String, value: Int) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setLongField(obj: Any, fieldName: String, value: Long) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setFloatField(obj: Any, fieldName: String, value: Float) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun setDoubleField(obj: Any, fieldName: String, value: Double) =
        setObjectField(obj, fieldName, value)

    @JvmStatic
    fun callStaticMethod(
        clazz: Class<*>,
        methodName: String,
        vararg args: Any?
    ): Any? {
        val parameterTypes: Array<Class<*>> =
            args.map { (it?.javaClass ?: Any::class.java) as Class<*> }.toTypedArray()
        return invoke(findMethodBestMatch(clazz, methodName, *parameterTypes), null, args)
    }

    @JvmStatic
    fun callStaticMethod(
        clazz: Class<*>,
        methodName: String,
        parameterTypes: Array<Class<*>>,
        vararg args: Any?
    ): Any? = invoke(findMethodBestMatch(clazz, methodName, *parameterTypes), null, args)

    @JvmStatic
    fun callMethod(
        obj: Any,
        methodName: String,
        vararg args: Any?
    ): Any? {
        val parameterTypes: Array<Class<*>> =
            args.map { (it?.javaClass ?: Any::class.java) as Class<*> }.toTypedArray()
        return invoke(findMethodBestMatch(obj.javaClass, methodName, *parameterTypes), obj, args)
    }

    @JvmStatic
    fun callMethod(
        obj: Any,
        methodName: String,
        parameterTypes: Array<Class<*>>,
        vararg args: Any?
    ): Any? = invoke(findMethodBestMatch(obj.javaClass, methodName, *parameterTypes), obj, args)

    private fun field(clazz: Class<*>, fieldName: String): Field =
        findField(clazz, fieldName).apply { isAccessible = true }

    private fun invoke(method: Method, receiver: Any?, args: Array<out Any?>): Any? {
        method.isAccessible = true
        return try {
            method.invoke(receiver, *args)
        } catch (e: InvocationTargetException) {
            throw e.cause ?: e
        }
    }

    private fun classesOf(clazz: Class<*>): Sequence<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        fun collect(type: Class<*>) {
            if (classes.contains(type)) return
            classes.add(type)
            type.interfaces.forEach { collect(it) }
            type.superclass?.let { collect(it) }
        }
        collect(clazz)
        return classes.asSequence()
    }

    private fun methodsOf(clazz: Class<*>): Sequence<Method> =
        classesOf(clazz).flatMap { it.declaredMethods.asSequence() }

    private fun isAssignable(parameterType: Class<*>, argumentType: Class<*>): Boolean {
        val boxed = when (parameterType.name) {
            "boolean" -> java.lang.Boolean::class.java
            "byte" -> java.lang.Byte::class.java
            "char" -> java.lang.Character::class.java
            "short" -> java.lang.Short::class.java
            "int" -> java.lang.Integer::class.java
            "long" -> java.lang.Long::class.java
            "float" -> java.lang.Float::class.java
            "double" -> java.lang.Double::class.java
            else -> parameterType
        }
        return boxed.isAssignableFrom(argumentType)
    }
}
