package com.houvven.androidc.information

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.pow
import kotlin.math.sqrt


@Suppress("unused", "MemberVisibilityCanBePrivate")
class DisplayInfoProvider private constructor(context: Context) {

    companion object {
        fun of(context: Context) = DisplayInfoProvider(context)
    }

    val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    @Suppress("DEPRECATION")
    val realMetrics = DisplayMetrics().apply { windowManager.defaultDisplay.getRealMetrics(this) }

    val realWidth = realMetrics.widthPixels
    val realHeight = realMetrics.heightPixels
    val realResolution = "${realHeight}x${realWidth}"
    val realDensity = realMetrics.density
    val realDensityDpi = realMetrics.densityDpi
    val realYDpi = realMetrics.ydpi
    val realXDpi = realMetrics.xdpi

    /**
     * Real width of the screen in inches
     */
    val realWidthInch = realWidth / realXDpi

    /**
     * Real height of the screen in inches
     */
    val realHeightInch = realHeight / realYDpi

    /**
     * Density of the screen in pixels.inch
     */
    val realDensityInch = realDensity / realXDpi

    /**
     * Density of the screen in pixels.inch
     */
    val realDensityInchDpi = realDensityDpi / realYDpi

    /**
     * Diagonal length of the screen in pixels.inch
     */
    val realDiagonalInch = sqrt(realWidthInch.pow(2) + realHeightInch.pow(2))

    /**
     * Diagonal length of the screen in pixels.cm
     */
    val realDiagonalCm = realDiagonalInch * 2.54


    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels
    val resolution = "${height}x${width}"
    val density = displayMetrics.density
    val densityDpi = displayMetrics.densityDpi
    val yDpi = displayMetrics.ydpi
    val xDpi = displayMetrics.xdpi

    /**
     * Width of the screen in inches
     */
    val widthInch = width / xDpi

    /**
     * Height of the screen in inches
     */
    val heightInch = height / yDpi

    /**
     * Density of the screen in pixels.inch
     */
    val densityInch = density / xDpi

    /**
     * Density of the screen in pixels.inch
     */
    val densityInchDpi = densityDpi / yDpi

    /**
     * Diagonal length of the screen in pixels.inch
     */
    val diagonalInch = sqrt(widthInch.pow(2) + heightInch.pow(2))

    /**
     * Diagonal length of the screen in pixels.cm
     */
    val diagonalCm = diagonalInch * 2.54
}