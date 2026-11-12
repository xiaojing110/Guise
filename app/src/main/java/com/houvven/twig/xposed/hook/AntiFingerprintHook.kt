package com.houvven.twig.xposed.hook

import android.os.Build
import android.webkit.WebView
import com.houvven.ktxposed.hook.method.beforeHookAllMethods
import com.houvven.ktxposed.hook.method.beforeHookMethod
import com.houvven.ktxposed.hook.method.findClassOrNull
import com.houvven.ktxposed.log.XposedLogger
import com.houvven.ktxposed.util.runXposedCatching
import com.houvven.twig.xposed.adapter.TwigHookAdapter
import de.robv.android.xposed.XC_MethodHook
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLConnection
import java.util.Locale
import kotlin.random.Random

/**
 * 防指纹上传。
 *
 * 1. HTTP：清洗请求头中会泄露设备指纹的字段（设备 ID / IMEI / 型号 / 品牌 /
 *    MAC / Android ID / OAID / Fingerprint 等），替换为当前伪装配置。
 * 2. WebView：在页面完成后注入 JS，对 Canvas / WebGL / AudioContext /
 *    navigator / screen 等浏览器指纹做一致性加噪，避免同一台设备被跨应用关联。
 *
 * 该功能只影响当前被 Hook 的应用，不会修改系统全局设置。
 */
class AntiFingerprintHook : TwigHookAdapter {

    override fun onHook() {
        if (config.ANTI_FINGERPRINT != true) return
        XposedLogger.i("Anti-Fingerprint: ENABLED")

        installHttpHeaderHooks()
        installOkHttpHooks()
        installWebViewHooks()
    }

    // ---------------------------------------------------------------- HTTP

    private val deviceIdHeaders = setOf(
        "device-id", "device_id", "x-device-id", "x-device_id", "x-deviceid",
        "android-id", "android_id", "x-android-id", "x-android_id",
        "oaid", "x-oaid", "vaid", "aaid", "udid", "x-udid",
        "idfa", "idfv", "openudid", "x-openudid"
    )

    private val imeiHeaders = setOf(
        "imei", "x-imei", "device-imei", "meid", "x-meid", "serial", "x-serial"
    )

    private val modelHeaders = setOf(
        "device-model", "device_model", "x-device-model", "x-device_model",
        "model", "x-model", "phone-model"
    )

    private val brandHeaders = setOf(
        "device-brand", "device_brand", "x-device-brand", "x-device_brand",
        "manufacturer", "x-manufacturer", "brand", "x-brand"
    )

    private val macHeaders = setOf("mac", "x-mac", "wifi-mac", "wifi_mac", "bssid", "x-bssid")

    private val fingerprintHeaders = setOf(
        "fingerprint", "x-fingerprint", "device-fingerprint", "device_fingerprint",
        "device-fingerprint-id", "x-fingerprint-id"
    )

    private val fallbackDeviceId: String by lazy {
        (1..16).map { "0123456789abcdef"[Random.nextInt(16)] }.joinToString("")
    }

    private fun installHttpHeaderHooks() = runXposedCatching {
        arrayOf(URLConnection::class.java, HttpURLConnection::class.java).forEach { clazz ->
            runXposedCatching {
                clazz.beforeHookMethod("setRequestProperty", String::class.java, String::class.java) { param ->
                    sanitizeParam(param)
                }
            }
            runXposedCatching {
                clazz.beforeHookMethod("addRequestProperty", String::class.java, String::class.java) { param ->
                    sanitizeParam(param)
                }
            }
        }
    }

    private fun sanitizeParam(param: XC_MethodHook.MethodHookParam) {
        val args = param.args
        val name = args.getOrNull(0) as? String ?: return
        val oldValue = args.getOrNull(1) as? String ?: return
        val newValue = sanitizeHeader(name, oldValue) ?: return
        if (newValue != oldValue) {
            args[1] = newValue
            XposedLogger.d("Anti-Fingerprint: sanitized header $name")
        }
    }

    private fun sanitizeHeader(name: String, value: String): String? {
        val key = name.lowercase(Locale.ROOT).trim()
        return when {
            key in deviceIdHeaders -> deviceId()
            key in imeiHeaders -> imei()
            key in modelHeaders -> model()
            key in brandHeaders -> brand()
            key in macHeaders -> mac()
            key in fingerprintHeaders || key.contains("fingerprint") -> fingerprint()
            key.contains("android_id") || key.contains("android-id") -> deviceId()
            key.contains("oaid") || key.contains("imei") || key.contains("udid") -> deviceId()
            else -> null
        }
    }

    private fun valueOf(value: Any, fallback: String): String =
        value.toString().takeIf { it.isNotBlank() && it != "-99" } ?: fallback

    private fun deviceId() = valueOf(config.DEVICE_ID, fallbackDeviceId)

    private fun imei() = valueOf(config.IMEI, fallbackDeviceId.filter { it.isDigit() }.padEnd(15, '0').take(15))

    private fun model(): String = valueOf(config.MODEL, Build.MODEL)

    private fun brand(): String = valueOf(config.MANUFACTURER, Build.MANUFACTURER)

    private fun mac(): String = valueOf(config.WIFI_MAC, "02:00:00:00:00:00")

    private fun fingerprint(): String {
        val configured = valueOf(config.FINGERPRINT, "")
        if (configured.isNotBlank()) return configured
        val deviceModel = model()
        val release = valueOf(config.RELEASE, Build.VERSION.RELEASE)
        return "${brand()}/$deviceModel/$deviceModel:$release/BP2A.260901.001/OS4.0.1.0.WPBCNXM:user/release-keys"
    }

    // -------------------------------------------------------------- OkHttp

    private fun installOkHttpHooks() = runXposedCatching {
        findClassOrNull("okhttp3.Request\$Builder")?.let { clazz ->
            listOf("header", "addHeader").forEach { method ->
                runXposedCatching {
                    clazz.beforeHookAllMethods(method) { param ->
                        if (param.args.size == 2 &&
                            param.args[0] is String && param.args[1] is String
                        ) {
                            sanitizeParam(param)
                        }
                    }
                }
            }
        }
        findClassOrNull("okhttp3.Headers\$Builder")?.let { clazz ->
            listOf("add", "set").forEach { method ->
                runXposedCatching {
                    clazz.beforeHookAllMethods(method) { param ->
                        if (param.args.size == 2 &&
                            param.args[0] is String && param.args[1] is String
                        ) {
                            sanitizeParam(param)
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------- WebView

    private fun installWebViewHooks() = runXposedCatching {
        val webViewClass = findClassOrNull("android.webkit.WebView")
            ?: return@runXposedCatching

        listOf("loadUrl", "loadData", "loadDataWithBaseURL").forEach { method ->
            runXposedCatching {
                webViewClass.beforeHookAllMethods(method) { param ->
                    val view = param.thisObject as? WebView ?: return@beforeHookAllMethods
                    view.postDelayed({ inject(view) }, 300L)
                }
            }
        }

        findClassOrNull("android.webkit.WebViewClient")?.let { clazz ->
            runXposedCatching {
                clazz.beforeHookAllMethods("onPageStarted") { param ->
                    val view = param.args.getOrNull(0) as? WebView ?: return@beforeHookAllMethods
                    view.post { inject(view) }
                }
            }
            runXposedCatching {
                clazz.beforeHookAllMethods("onPageFinished") { param ->
                    val view = param.args.getOrNull(0) as? WebView ?: return@beforeHookAllMethods
                    inject(view)
                }
            }
        }

        findClassOrNull("android.webkit.WebChromeClient")?.let { clazz ->
            runXposedCatching {
                clazz.beforeHookAllMethods("onProgressChanged") { param ->
                    val view = param.args.getOrNull(0) as? WebView ?: return@beforeHookAllMethods
                    val progress = param.args.getOrNull(1) as? Int ?: -1
                    if (progress >= 100) inject(view)
                }
            }
        }

        findClassOrNull("android.webkit.WebSettings")?.let { clazz ->
            runXposedCatching {
                clazz.beforeHookAllMethods("getUserAgentString") { param ->
                    param.result = userAgent()
                }
            }
        }

        XposedLogger.d("Anti-Fingerprint: WebView hooks installed")
    }

    private fun userAgent(): String {
        val release = valueOf(config.RELEASE, Build.VERSION.RELEASE)
        val deviceModel = model()
        val buildId = "BP2A.260901.001"
        return "Mozilla/5.0 (Linux; Android $release; $deviceModel Build/$buildId; wv) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/131.0.0.0 Mobile Safari/537.36"
    }

    @Volatile
    private var script: String? = null

    private fun inject(view: WebView) {
        val js = script ?: buildScript().also { script = it }
        runCatching { view.evaluateJavascript(js, null) }
            .onFailure { XposedLogger.d("Anti-Fingerprint: inject failed: ${it.message}") }
    }

    private fun q(value: String): String = JSONObject.quote(value)

    private fun buildScript(): String {
        val deviceModel = model()
        val ua = userAgent()
        val width = valueOf(config.DISPLAY_WIDTH, "1080").toIntOrNull() ?: 1080
        val height = valueOf(config.DISPLAY_HEIGHT, "2400").toIntOrNull() ?: 2400
        val seed = (deviceModel.hashCode() and 0x7) + 1
        val n1 = seed
        val n2 = (seed * 3 + 1) and 0x7
        val n3 = (seed * 5 + 2) and 0x7

        return """
            (function () {
              try {
                if (window.__guiseAfpInstalled) return;
                window.__guiseAfpInstalled = true;
                var C = {
                  ua: ${q(ua)},
                  platform: "Linux armv8l",
                  vendor: ${q(brand())},
                  language: ${q(valueOf(config.LANGUAGE, "zh-CN"))},
                  w: $width,
                  h: $height,
                  density: 2.75,
                  cores: 8,
                  memory: 8,
                  glV: ${q(valueOf(config.GPU_VENDOR, "Qualcomm"))},
                  glR: ${q(valueOf(config.GPU_RENDERER, "Adreno (TM) 850"))},
                  deviceId: ${q(deviceId())},
                  imei: ${q(imei())},
                  model: ${q(model())},
                  brand: ${q(brand())},
                  n1: $n1, n2: $n2, n3: $n3
                };
                function sanitizeHeader(k, v) {
                  try {
                    var lk = String(k).toLowerCase();
                    if (/imei|meid|serial/.test(lk)) return C.imei;
                    if (/device[-_]?id|oaid|udid|android[-_]?id|idfa|idfv/.test(lk)) return C.deviceId;
                    if (/model/.test(lk)) return C.model;
                    if (/brand|manufacturer/.test(lk)) return C.brand;
                    if (/fingerprint/.test(lk)) return C.brand + "/" + C.model;
                  } catch (e) {}
                  return v;
                }
                try {
                  var origSetHeader = XMLHttpRequest.prototype.setRequestHeader;
                  XMLHttpRequest.prototype.setRequestHeader = function (k, v) {
                    return origSetHeader.call(this, k, sanitizeHeader(k, v));
                  };
                } catch (e) {}
                function def(o, k, v) {
                  try {
                    Object.defineProperty(o, k, { get: function () { return v; }, configurable: true });
                  } catch (e) {}
                }
                try {
                  def(navigator, "userAgent", C.ua);
                  def(navigator, "appVersion", C.ua.replace("Mozilla/", ""));
                  def(navigator, "platform", C.platform);
                  def(navigator, "vendor", C.vendor);
                  def(navigator, "language", C.language);
                  def(navigator, "languages", [C.language]);
                  def(navigator, "hardwareConcurrency", C.cores);
                  def(navigator, "deviceMemory", C.memory);
                  def(navigator, "maxTouchPoints", 10);
                  def(navigator, "webdriver", false);
                } catch (e) {}
                try {
                  def(screen, "width", C.w);
                  def(screen, "height", C.h);
                  def(screen, "availWidth", C.w);
                  def(screen, "availHeight", C.h);
                  def(screen, "colorDepth", 24);
                  def(screen, "pixelDepth", 24);
                  def(window, "devicePixelRatio", C.density);
                } catch (e) {}
                try {
                  var proto = CanvasRenderingContext2D.prototype;
                  var origGetImageData = proto.getImageData;
                  proto.getImageData = function () {
                    var d = origGetImageData.apply(this, arguments);
                    var a = d.data;
                    for (var i = 0; i < a.length; i += 4) {
                      a[i] = (a[i] + C.n1) & 255;
                      a[i + 1] = (a[i + 1] + C.n2) & 255;
                      a[i + 2] = (a[i + 2] + C.n3) & 255;
                    }
                    return d;
                  };
                } catch (e) {}
                try {
                  var origToDataURL = HTMLCanvasElement.prototype.toDataURL;
                  HTMLCanvasElement.prototype.toDataURL = function () {
                    try {
                      if (!this.__guiseNoised && this.width && this.height) {
                        this.__guiseNoised = true;
                        var ctx = this.getContext("2d");
                        if (ctx) {
                          var img = ctx.getImageData(0, 0, this.width, this.height);
                          var a = img.data;
                          for (var i = 0; i < a.length; i += 4) {
                            a[i] = (a[i] + C.n1) & 255;
                            a[i + 1] = (a[i + 1] + C.n2) & 255;
                            a[i + 2] = (a[i + 2] + C.n3) & 255;
                          }
                          ctx.putImageData(img, 0, 0);
                        }
                      }
                    } catch (e) {}
                    return origToDataURL.apply(this, arguments);
                  };
                } catch (e) {}
                try {
                  var glGetParameter = WebGLRenderingContext.prototype.getParameter;
                  WebGLRenderingContext.prototype.getParameter = function (p) {
                    if (p === 37445) return C.glV;
                    if (p === 37446) return C.glR;
                    return glGetParameter.apply(this, arguments);
                  };
                } catch (e) {}
                try {
                  if (window.WebGL2RenderingContext) {
                    var gl2GetParameter = WebGL2RenderingContext.prototype.getParameter;
                    WebGL2RenderingContext.prototype.getParameter = function (p) {
                      if (p === 37445) return C.glV;
                      if (p === 37446) return C.glR;
                      return gl2GetParameter.apply(this, arguments);
                    };
                  }
                } catch (e) {}
                try {
                  var origAudio = AnalyserNode.prototype.getFloatFrequencyData;
                  AnalyserNode.prototype.getFloatFrequencyData = function (arr) {
                    origAudio.apply(this, arguments);
                    for (var i = 0; i < arr.length; i++) {
                      arr[i] = arr[i] + ((i % 7) - 3) * 0.01;
                    }
                  };
                } catch (e) {}
              } catch (e) {}
            })();
        """.trimIndent()
    }
}
