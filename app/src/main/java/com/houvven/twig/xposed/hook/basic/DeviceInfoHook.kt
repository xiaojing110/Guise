package com.houvven.twig.xposed.hook.basic

import android.os.Build
import com.houvven.ktxposed.LocalHookParam
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.ktxposed.hook.method.setStaticField
import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import com.houvven.twig.xposed.setSystemProp
import java.lang.reflect.Proxy
import javax.microedition.khronos.opengles.GL10


class DeviceInfoHook : TwigHookAdapter {

    override fun onHook() {
        runXposedCatching {
            Proxy.newProxyInstance(
                LocalHookParam.lpparam.classLoader,
                arrayOf(GL10::class.java)
            ) { obj, _, _ ->
                obj.javaClass.beforeHookMethod("glGetString", Int::class.java) { param ->
                    param.result = "Demo"
                }
            }
        }

        /*
        findClassOrNull("android.opengl.GLSurfaceView.EglHelper").let { eglHelper ->
            eglHelper?.beforeHookAllMethods("createGL") { param ->
                val gL10 = param.result as GL10
                XposedLogger.d("GLSurfaceView.EglHelper.createGL: ${gL10.glGetString(GL10.GL_VENDOR)}")
                XposedLogger.d("GLSurfaceView.EglHelper.createGL: ${gL10.glGetString(GL10.GL_RENDERER)}")
                XposedLogger.d("GLSurfaceView.EglHelper.createGL: ${gL10.glGetString(GL10.GL_VERSION)}")
            } ?: XposedLogger.d("GLSurfaceView.EglHelper not found")
        }
        */


        config.run {
            listOf(
                MANUFACTURER to arrayOf(default.MANUFACTURER, "BRAND", "MANUFACTURER"),
                MODEL to arrayOf(default.MODEL, "MODEL"),
                PRODUCT to arrayOf(default.PRODUCT, "PRODUCT"),
                DEVICE to arrayOf(default.DEVICE, "DEVICE"),
                BOARD to arrayOf(default.BOARD, "BOARD")
            ).forEach { (value, names) ->
                if (value == names.first()) return@forEach
                names.forEachIndexed { index, s ->
                    if (index == 0) return@forEachIndexed
                    runXposedCatching { Build::class.setStaticField(s, value) }
                }
            }

            if (CHARACTERISTICS != default.CHARACTERISTICS)
                setSystemProp("ro.build.characteristics", CHARACTERISTICS)

            if (RADIO_VERSION != default.RADIO_VERSION)
                Build::class.replaceMethodResult("getRadioVersion") { RADIO_VERSION }
        }
    }
}