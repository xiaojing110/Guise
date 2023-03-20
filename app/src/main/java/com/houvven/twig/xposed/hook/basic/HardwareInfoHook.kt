package com.houvven.twig.xposed.hook.basic

import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Parcel
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowMetrics
import com.houvven.ktxposed.hook.method.afterHookAllConstructors
import com.houvven.ktxposed.hook.method.afterHookMethod
import com.houvven.ktxposed.hook.method.afterSetResult
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.findClassOrNull
import com.houvven.ktxposed.hook.method.findMethodExact
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.ktxposed.hook.method.setInstanceField
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import javax.microedition.khronos.opengles.GL10

class HardwareInfoHook : TwigHookAdapter {

    override fun onHook() {
        hookDisplayInfo()
        hookGpuInfo()
    }


    private fun hookDisplayInfo() = config.run {
        val xIsOk = DISPLAY_WIDTH != default.DISPLAY_WIDTH
        val yIsOk = DISPLAY_HEIGHT != default.DISPLAY_HEIGHT
        val dpiIsOk = DISPLAY_DENSITY_DPI != default.DISPLAY_DENSITY_DPI
        val fontScaleIsOk = DISPLAY_FONT_SCALE!= default.DISPLAY_FONT_SCALE
        Display::class.run {
            if (xIsOk) findMethodExact("getWidth").afterSetResult { DISPLAY_WIDTH }
            if (yIsOk) findMethodExact("getHeight").afterSetResult { DISPLAY_HEIGHT }

            afterHookMethod("getSize", Point::class) {
                val point = it.args.first() as Point
                if (xIsOk) point.x = DISPLAY_WIDTH
                if (yIsOk) point.y = DISPLAY_HEIGHT
            }

            afterHookMethod("getSupportedModes") {
                @Suppress("UNCHECKED_CAST")
                val modes = it.result as Array<Display.Mode>
                if (modes.isNotEmpty()) {
                    val mode = modes.first()
                    if (xIsOk) mode.setInstanceField("mWidth", DISPLAY_WIDTH)
                    if (yIsOk) mode.setInstanceField("mHeight", DISPLAY_HEIGHT)
                    modes[0] = mode
                }
                it.result = modes
            }

            listOf("getMetrics", "getRealMetrics").forEach { methodName ->
                afterHookMethod(methodName, DisplayMetrics::class) { param ->
                    val metrics = param.args.first() as DisplayMetrics
                    if (xIsOk) metrics.widthPixels = DISPLAY_WIDTH
                    if (yIsOk) metrics.heightPixels = DISPLAY_HEIGHT
                    if (dpiIsOk) metrics.densityDpi = DISPLAY_DENSITY_DPI
                }
            }

        }


         Configuration::class.run {
            afterHookMethod("setToDefaults") {
                val configuration = it.thisObject as Configuration
                if (dpiIsOk) configuration.densityDpi = DISPLAY_DENSITY_DPI
                if (fontScaleIsOk) configuration.fontScale = DISPLAY_FONT_SCALE
            }

            beforeHookMethod("setTo", Configuration::class) {
                val configuration = it.args.first() as Configuration
                if (dpiIsOk) configuration.densityDpi = DISPLAY_DENSITY_DPI
                if (fontScaleIsOk) configuration.fontScale = DISPLAY_FONT_SCALE
            }

            afterHookMethod("readFromParcel", Parcel::class) {
                val configuration = it.thisObject as Configuration
                if (dpiIsOk) configuration.densityDpi = DISPLAY_DENSITY_DPI
                if (fontScaleIsOk) configuration.fontScale = DISPLAY_FONT_SCALE
            }
        }

        if ((xIsOk && yIsOk) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val rect = Rect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT)
            WindowMetrics::class.run {
                afterHookAllConstructors { it.thisObject.setInstanceField("mBounds", rect) }
                replaceMethodResult("getBounds") { rect }
            }
        }

    }


    private fun hookGpuInfo() = config.run {
        arrayOf(
            // "com.google.android.gles_jni.EGLImpl",
            "com.google.android.gles_jni.GLImpl",
        ).forEach {
            findClassOrNull(it) {
                XposedLogger.d("$it -> Ok")
                beforeHookMethod("glGetString", Int::class.java) { param ->
                    val name = param.args.first() as Int
                    if (name == GL10.GL_RENDERER && GPU_RENDERER != default.GPU_RENDERER) {
                        param.result = GPU_RENDERER
                    } else if (name == GL10.GL_VENDOR && GPU_VENDOR != default.GPU_VENDOR) {
                        param.result = GPU_VENDOR
                    } else if (name == GL10.GL_VERSION && GPU_VERSION != default.GPU_VERSION) {
                        param.result = GPU_VERSION
                    }else if (name == GL10.GL_EXTENSIONS && GPU_EXTENSIONS != default.GPU_EXTENSIONS) {
                        param.result = GPU_EXTENSIONS
                    }
                }
            }
        }
    }


}