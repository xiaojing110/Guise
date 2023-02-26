package com.houvven.guise.ui.route.editor

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.houvven.androidc.ktx.ifTrue
import java.net.URLDecoder


@Suppress("SpellCheckingInspection")
private const val KEY = "RTNBZ-76S64-IYWUB-D5WRM-JBXEK-R6FLS"
private const val REFERER = "Guise"
private const val URL =
    "https://apis.map.qq.com/tools/locpicker?search=1&type=0&backurl=http://callback&key=$KEY&referer=$REFERER"
// "https://www.baidu.com"

@Composable
@RequiresPermission("android.permission.INTERNET")
fun TencentMapPointSelection() {
    val state = rememberWebViewState(url = URL)

    WebView(
        state = state,
        onCreated = { webView ->
            webView.settings.run {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
            }
        },
        client = object : AccompanistWebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url
                url?.toString()?.startsWith("http://callback")?.ifTrue {
                    val decode: String = URLDecoder.decode(url.toString(), "UTF-8")
                    val uri = Uri.parse(decode)

                    @Suppress("SpellCheckingInspection")
                    val latng = uri.getQueryParameter("latng")
                    val (lat, lng) = latng!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }

                    @Suppress("SpellCheckingInspection")
                    val address = uri.getQueryParameter("addr")

                    Log.d("TencentMapPointSelection", "lat: $lat, lng: $lng, address: $address")
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    )
}