package com.houvven.twig.xposed.hook.location

import android.location.GnssStatus
import android.location.GpsStatus
import android.location.GpsStatus.GPS_EVENT_FIRST_FIX
import android.location.GpsStatus.GPS_EVENT_STARTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.SystemClock
import com.houvven.ktxposed.hook.method.afterHookMethod
import com.houvven.ktxposed.hook.method.beforeHookAllMethods
import com.houvven.ktxposed.hook.method.beforeHookConstructor
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.callMethod
import com.houvven.ktxposed.hook.method.callStaticMethod
import com.houvven.ktxposed.hook.method.findMethodExactOrNull
import com.houvven.ktxposed.hook.method.hookAllMethods
import com.houvven.ktxposed.hook.method.replaceAllMethodsResult
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import java.lang.reflect.Proxy

@Suppress("DEPRECATION")
class LocationHook : TwigHookAdapter {

    private val longitude = config.LONGITUDE
    private val latitude = config.LATITUDE

    override fun onHook() {
        if (longitude == default.LONGITUDE || latitude == default.LATITUDE) {
            return
        }

        DisableOtherLocation.onHook()
        this.hookGpuProviderStatus()
        fakeLatlng()
        hookLocationUpdate()
        hookLastLocation()
        hookGpsStatusListener()
        hookGpsStatus()
        hookGnssStatus()
    }

    private fun fakeLatlng() {
        Location::class.run {
            replaceAllMethodsResult("getLatitude") { latitude }
            replaceAllMethodsResult("getLongitude") { longitude }
            beforeHookMethod("setLongitude", Double::class) { it.args[0] = longitude }
            beforeHookMethod("setLatitude", Double::class) { it.args[0] = latitude }
        }
    }

    private fun hookLastLocation() {
        arrayOf(
            "getLastLocation",
            "getLastKnownLocation"
        ).forEach {
            LocationManager::class.replaceAllMethodsResult(it) {
                modifyLocation(
                    Location(
                        LocationManager.GPS_PROVIDER
                    )
                )
            }
        }
    }

    private fun hookLocationUpdate() {
        LocationManager::class.java.hookAllMethods {
            beforeHook { param ->
                val locationListener =
                    param.args.find { it is LocationListener } as LocationListener?
                        ?: return@beforeHook
                val proxy = Proxy.newProxyInstance(
                    locationListener.javaClass.classLoader,
                    arrayOf(LocationListener::class.java)
                ) { _, method, args ->
                    if (method.name == "onLocationChanged" && args.first() is Location) {
                        XposedLogger.d("onLocationChanged")
                        locationListener.onLocationChanged(modifyLocation(args.first() as Location))
                    }
                }
                param.args[param.args.indexOfLast { it is LocationListener }] = proxy
                locationListener.onLocationChanged(modifyLocation(Location(LocationManager.GPS_PROVIDER)))
            }
        }
    }

    private fun modifyLocation(location: Location): Location {
        return location.also {
            it.longitude = longitude
            it.latitude = latitude
            it.provider = LocationManager.GPS_PROVIDER
            it.accuracy = 1.0f
            it.time = System.currentTimeMillis()
            it.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
    }

    private fun hookGpuProviderStatus() {
        LocationManager::class.java.run {
            replaceAllMethodsResult("isLocationEnabledForUser") { true }
            arrayOf(
                "isProviderEnabledForUser",
                "hasProvider"
            ).forEach {
                beforeHookAllMethods(it) { param ->
                    when (param.args[0] as String) {
                        LocationManager.GPS_PROVIDER -> param.result = true
                        LocationManager.FUSED_PROVIDER,
                        LocationManager.NETWORK_PROVIDER,
                        LocationManager.PASSIVE_PROVIDER,
                        -> param.result = false
                    }
                }
            }

            arrayOf(
                "getProviders",
                "getAllProviders"
            ).forEach {
                replaceAllMethodsResult(it) { listOf(LocationManager.GPS_PROVIDER) }
            }

            replaceAllMethodsResult("getBestProvider") { LocationManager.GPS_PROVIDER }
        }
    }


    private val svCount = 43
    private val svidWithFlags = (0..43).map { it }.toIntArray()
    private val cn0s = (0..43).map { 0F }.toFloatArray()
    private val elevations = cn0s.clone()
    private val azimuths = cn0s.clone()
    private val carrierFrequencies = cn0s.clone()
    private val basebandCn0DbHzs = cn0s.clone()

    private fun hookGpsStatusListener() {
        LocationManager::class.java.run {
            afterHookMethod(
                methodName = "addGpsStatusListener",
                GpsStatus.Listener::class.java
            ) { param ->
                (param.args[0] as GpsStatus.Listener?)?.run {
                    callMethod("onGpsStatusChanged", GPS_EVENT_STARTED)
                    callMethod("onGpsStatusChanged", GPS_EVENT_FIRST_FIX)
                }
            }

            replaceAllMethodsResult("addNmeaListener") { false }
        }
    }

    private fun hookGpsStatus() {
        LocationManager::class.java.beforeHookMethod(
            methodName = "getGpsStatus",
            GpsStatus::class.java
        ) { param ->
            val status = param.args[0] as GpsStatus? ?: return@beforeHookMethod

            val method = GpsStatus::class.java.findMethodExactOrNull(
                "setStatus",
                Int::class.java,
                Array::class.java,
                Array::class.java,
                Array::class.java,
                Array::class.java
            )

            val method2 = GpsStatus::class.java.findMethodExactOrNull(
                "setStatus",
                GnssStatus::class.java,
                Int::class.java
            )

            if (method == null && method2 == null) {
                return@beforeHookMethod
            }

            {
                method?.invoke(status, svCount, svidWithFlags, cn0s, elevations, azimuths)
                GnssStatus::class.java.callStaticMethod(
                    "wrap",
                    svCount,
                    svidWithFlags,
                    cn0s,
                    elevations,
                    azimuths,
                    carrierFrequencies,
                    basebandCn0DbHzs
                ).let {
                    val gnss = it.getOrThrow()
                    method2?.invoke(status, gnss, System.currentTimeMillis().toInt())
                }
            }.let {
                it()
                param.args[0] = status
                param.result = status
                it()
                param.result = status
            }
        }
    }

    private fun hookGnssStatus() {
        GnssStatus::class.java.beforeHookConstructor(
            Int::class.java,
            IntArray::class.java,
            FloatArray::class.java,
            FloatArray::class.java,
            FloatArray::class.java,
            FloatArray::class.java,
            FloatArray::class.java
        ) {
            it.args[0] = svCount
            it.args[1] = svidWithFlags
            it.args[2] = cn0s
            it.args[3] = elevations
            it.args[4] = azimuths
            it.args[5] = carrierFrequencies
            it.args[6] = basebandCn0DbHzs
        }
    }

}
