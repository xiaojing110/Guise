package com.houvven.twig.ui.route.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.houvven.androidc.ktx.ifTrue
import java.net.URLDecoder

class LatLng(val lat: Double, val lng: Double)

object MapPointSelectionParams {
    internal var onLeaving: (LatLng) -> Unit = {}

    fun setOnLeaving(block: (LatLng) -> Unit) {
        onLeaving = block
    }
}


@Suppress("SpellCheckingInspection")
private const val KEY = "RTNBZ-76S64-IYWUB-D5WRM-JBXEK-R6FLS"
private const val URL =
    "https://apis.map.qq.com/tools/locpicker?search=1&type=0&backurl=http://callback&key=$KEY&referer=MyApp"

@Composable
@RequiresPermission("android.permission.INTERNET")
fun TencentMapPointSelection() {
    val state = rememberWebViewState(url = URL)
    var latLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    DisposableEffect(Unit) {
        onDispose {
            MapPointSelectionParams.onLeaving(latLng)
        }
    }

    WebView(
        modifier = Modifier.fillMaxSize(),
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
                    latLng = LatLng(lat.toDouble(), lng.toDouble())
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    )
}