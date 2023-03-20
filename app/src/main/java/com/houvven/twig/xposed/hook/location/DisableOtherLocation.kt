package com.houvven.twig.xposed.hook.location

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.INCLUDE_LOCATION_DATA_NONE
import com.houvven.ktxposed.hook.method.replaceAllMethodsResult
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import com.houvven.twig.xposed.hook.SimInfoHook

internal object DisableOtherLocation : TwigHookAdapter {
    override fun onHook() = config.run {
        if (DISABLE_WIFI_LOCATION) wifi()
        if (DISABLE_TEL_LOCATION) tel()
    }


    private fun wifi() {
        WifiManager::class.java.run {
            replaceMethodResult("getScanResults") { emptyList<ScanResult>() }
            replaceMethodResult("isWifiEnabled") { false }
            replaceMethodResult("isScanAlwaysAvailable") { false }
            replaceMethodResult("getWifiState") { WifiManager.WIFI_STATE_DISABLED }
        }
        WifiInfo::class.java.run {
            replaceMethodResult("getSSID") { "<unknown ssid>" }
            replaceMethodResult("getBSSID") { "02:00:00:00:00:00" }
            replaceMethodResult("getMacAddress") { "02:00:00:00:00:00" }
        }
    }

    private fun tel() {
        TelephonyManager::class.java.run {
            arrayOf(
                "getCellLocation",
                "getAllCellInfo",
                "getNeighboringCellInfo",
                "getLastKnownCellIdentity",
            ).forEach {
                replaceAllMethodsResult(it) { null }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                replaceMethodResult("getLocationData") { INCLUDE_LOCATION_DATA_NONE }
            }

            SimInfoHook.setLac(-1)
            SimInfoHook.setCid(-1)
        }

        arrayOf(
            "onCellLocationChanged", "onCellInfoChanged"
        ).forEach {
            PhoneStateListener::class.java.run {
                replaceAllMethodsResult(it) { null }
            }
        }
    }

}