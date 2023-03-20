package com.houvven.twig.xposed

import android.os.Build
import com.houvven.ktxposed.hook.method.beforeHookAllMethods
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.findClass
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.ktxposed.util.runXposedCatching

fun setSystemProp(name: String, value: String) = runXposedCatching {
    findClass("android.os.SystemProperties")
        .beforeHookAllMethods("get") { param ->
            if (param.args.any { it == name } ) {
                XposedLogger.d("setSystemProp: $name = $value")
                param.result = value
            }
        }
}
