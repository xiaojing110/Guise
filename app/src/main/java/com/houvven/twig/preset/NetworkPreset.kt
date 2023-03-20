package com.houvven.twig.preset

import android.telephony.TelephonyManager
import com.houvven.twig.preset.adapter.PresetAdapter
import com.houvven.twig.xposed.HookValues

enum class NetworkType(
    override val label: String,
    override val value: String
) : PresetAdapter {
    UNHOOK("UNHOOK", ""),
    NONE("NONE", HookValues.NET_TYPE_NONE.toString()),
    WIFI("Wifi", HookValues.NET_TYPE_WIFI.toString()),
    MOBILE("mobile", HookValues.NET_TYPE_MOBILE.toString())
}


@Suppress("unused", "SpellCheckingInspection")
enum class MobileNetworkType(
    override val label: String,
    override val value: String
) : PresetAdapter {
    UNKNOWN("UNKNOWN", TelephonyManager.NETWORK_TYPE_UNKNOWN.toString()),
    LTE("LTE", TelephonyManager.NETWORK_TYPE_LTE.toString()),
    NR("NR", 20.toString()),
    CDMA("CDMA", TelephonyManager.NETWORK_TYPE_CDMA.toString()),
    TD_SCDMA("TD_SCDMA", TelephonyManager.NETWORK_TYPE_TD_SCDMA.toString()),
    GSM("GSM", TelephonyManager.NETWORK_TYPE_GSM.toString()),
    UMTS("UMTS", TelephonyManager.NETWORK_TYPE_UMTS.toString()),
    HSDPA("HSDPA", TelephonyManager.NETWORK_TYPE_HSDPA.toString()),
    GRPS("GRPS", TelephonyManager.NETWORK_TYPE_GPRS.toString()),
    ;
}