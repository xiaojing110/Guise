package com.houvven.twig.xposed.hook

import android.os.Build
import android.telephony.CellIdentityCdma
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityLte
import android.telephony.CellIdentityNr
import android.telephony.CellIdentityTdscdma
import android.telephony.CellIdentityWcdma
import android.telephony.SubscriptionInfo
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import com.houvven.ktxposed.hook.method.findMethodExactOrNull
import com.houvven.ktxposed.hook.method.replaceAllMethodsResult
import com.houvven.ktxposed.hook.method.replaceMethodResult
import com.houvven.ktxposed.hook.method.replaceResult
import com.houvven.twig.xposed.adapter.TwigHookAdapter

@Suppress("DEPRECATION")
class SimInfoHook : TwigHookAdapter {


    override fun onHook() {
        hookSimOperator()
        hookSimOperatorName()
        hookCountryIso()
        hookCid()
        hookLac()
    }


    private fun hookSimOperator() {
        val simOperator = config.SIM_OPERATOR
        if (simOperator == default.SIM_OPERATOR) return

        val mcc = simOperator.substring(0, 3)
        val mnc = simOperator.substring(3)
        val mccInt = mcc.toIntOrNull()
        val mncInt = mnc.toIntOrNull()

        arrayOf(
            "getSimOperatorNumericForPhone",
            "getNetworkOperatorForPhone",
            "getSimOperator",
            "getNetworkOperator"
        ).forEach {
            TelephonyManager::class.replaceAllMethodsResult(it) { simOperator }
        }

        val classes = mutableListOf(
            SubscriptionInfo::class,
            CellIdentityCdma::class,
            CellIdentityGsm::class,
            CellIdentityLte::class,
            CellIdentityWcdma::class
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            classes.add(CellIdentityNr::class)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            classes.add(CellIdentityTdscdma::class)
        }
        classes.forEach {
            it.run {
                findMethodExactOrNull("getMcc")?.replaceResult { mccInt }
                findMethodExactOrNull("getMnc")?.replaceResult { mncInt }
                findMethodExactOrNull("getMccString")?.replaceResult { mcc }
                findMethodExactOrNull("getMncString")?.replaceResult { mnc }
            }
        }
    }


    private fun hookSimOperatorName() {
        val operatorName = config.SIM_OPERATOR_NAME
        if (operatorName == default.SIM_OPERATOR_NAME) return
        listOf(
            TelephonyManager::class.java to "getSimOperatorName",
            TelephonyManager::class.java to "getSimOperatorNameForPhone",
            TelephonyManager::class.java to "getNetworkOperatorName",
            TelephonyManager::class.java to "getNetworkOperatorNameForPhone",
            SubscriptionInfo::class.java to "getCarrierName",
            SubscriptionInfo::class.java to "getDisplayName",
        ).forEach { it.first.replaceAllMethodsResult(it.second) { operatorName } }
    }


    private fun hookCountryIso() {
        val country = config.SIM_COUNTRY_ISO
        if (country == default.SIM_COUNTRY_ISO) return
        listOf(
            TelephonyManager::class to "getSimCountryIso",
            TelephonyManager::class to "getSimCountryIsoForPhone",
            TelephonyManager::class to "getNetworkCountryIso",
            TelephonyManager::class to "getNetworkCountryIsoForPhone",
            SubscriptionInfo::class to "getCountryIso"
        ).forEach { it.first.replaceAllMethodsResult(it.second) { country } }
    }

    private fun hookLac() {
        if (config.LAC == default.LAC && config.DISABLE_TEL_LOCATION.not()) return
        setLac(config.LAC)
    }


    private fun hookCid() {
        if (config.CID == default.CID && config.DISABLE_TEL_LOCATION.not()) return
        setCid(config.CID)
    }

    companion object {
        fun setLac(lac: Int) {
            GsmCellLocation::class.java.replaceMethodResult("getLac") { lac }
            CellIdentityGsm::class.java.replaceMethodResult("getLac") { lac }
            CellIdentityLte::class.java.replaceMethodResult("getTac") { lac }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                CellIdentityTdscdma::class.java.replaceMethodResult("getLac") { lac }
            }
            CellIdentityCdma::class.java.replaceMethodResult("getNetworkId") { lac }
            CellIdentityWcdma::class.java.replaceMethodResult("getLac") { lac }
        }

        fun setCid(cid: Int) {
            GsmCellLocation::class.java.replaceMethodResult("getCid") { cid }
            CellIdentityGsm::class.java.replaceMethodResult("getCid") { cid }
            CellIdentityLte::class.java.replaceMethodResult("getCi") { cid }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                CellIdentityTdscdma::class.java.replaceMethodResult("getCid") { cid }
            }
            CellIdentityCdma::class.java.replaceMethodResult("getBasestationId") { cid }
            CellIdentityWcdma::class.java.replaceMethodResult("getCid") { cid }
        }
    }
}