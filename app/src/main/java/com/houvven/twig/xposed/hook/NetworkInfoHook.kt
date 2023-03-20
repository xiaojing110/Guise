package com.houvven.twig.xposed.hook

import android.bluetooth.BluetoothDevice
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.twig.xposed.HookValues
import com.houvven.twig.xposed.adapter.TwigHookAdapter

class NetworkInfoHook : TwigHookAdapter {

    override fun onHook() {
        hookNetworkType()
        hookWifiInfo()
        bluetoothInfo()
    }

    @Suppress("DEPRECATION")
    private fun hookNetworkType() = config.run {
        if (NETWORK_TYPE != default.NETWORK_TYPE)
            NetworkInfo::class.replaceMethodResult("getType") { NETWORK_TYPE }

        if (!DISABLE_WIFI_LOCATION) {
            if (NETWORK_TYPE == HookValues.NET_TYPE_WIFI) {
                WifiManager::class.run {
                    beforeHookMethod("isWifiEnabled") {
                        it.result = true
                    }
                    beforeHookMethod("getWifiState") {
                        it.result = WifiManager.WIFI_STATE_ENABLED
                    }
                }
            }
        }

        if (MOBILE_NETWORK_TYPE != default.MOBILE_NETWORK_TYPE)
            TelephonyManager::class.beforeHookMethod("getNetworkType") {
                it.result = MOBILE_NETWORK_TYPE
            }
    }


    private fun hookWifiInfo() = config.run {
        if (DISABLE_WIFI_LOCATION) return
        WifiInfo::class.run {
            if (WIFI_SSID != default.WIFI_SSID) replaceMethodResult("getSSID") { WIFI_SSID }
            if (WIFI_BSSID != default.WIFI_BSSID) replaceMethodResult("getBSSID") { WIFI_BSSID }
            if (WIFI_MAC != default.WIFI_MAC) replaceMethodResult("getMacAddress") { WIFI_MAC }
        }
    }


    private fun bluetoothInfo() = config.run {
        BluetoothDevice::class.run {
            if (BLUETOOTH_NAME != default.BLUETOOTH_NAME) replaceMethodResult("getName") { BLUETOOTH_NAME }
            if (BLUETOOTH_MAC != default.BLUETOOTH_MAC) replaceMethodResult("getAddress") { BLUETOOTH_MAC }
        }
    }

}