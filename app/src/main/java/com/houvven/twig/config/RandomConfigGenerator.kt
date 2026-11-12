package com.houvven.twig.config

import android.content.Context
import com.houvven.twig.preset.device.Device
import com.houvven.twig.preset.device.DeviceDBHelper
import kotlin.random.Random

/**
 * 一键随机：从设备库中随机选取一台 2026 年机型，生成一套完整、互相自洽的
 * Build / 硬件 / 网络 / 标识配置。
 *
 * 生成结果会直接写回 [TwigConfig.Manager] 并保存，避免「随机后丢失」。
 */
@Suppress("unused")
object RandomConfigGenerator {

    private data class Gpu(val vendor: String, val renderer: String)

    private data class Screen(val width: Int, val height: Int, val dpi: Int)

    private data class Carrier(val name: String, val code: String, val country: String)

    private val gpus = listOf(
        Gpu("Qualcomm", "Adreno (TM) 850"),
        Gpu("Qualcomm", "Adreno (TM) 840"),
        Gpu("Qualcomm", "Adreno (TM) 830"),
        Gpu("ARM", "Mali-G925-Immortalis MC12"),
        Gpu("ARM", "Mali-G720-Immortalis MC12"),
        Gpu("ARM", "Mali-G715-Immortalis MC11"),
        Gpu("Samsung", "Xclipse 950"),
        Gpu("Huawei", "Maleoon 920"),
        Gpu("Apple", "Apple GPU")
    )

    private val screens = listOf(
        Screen(1080, 2400, 420),
        Screen(1220, 2712, 440),
        Screen(1260, 2800, 450),
        Screen(1208, 2624, 480),
        Screen(1440, 3200, 560),
        Screen(1176, 2400, 440),
        Screen(1080, 2340, 400),
        Screen(1280, 2856, 480)
    )

    private val foldScreens = listOf(
        Screen(1672, 2364, 420),
        Screen(1812, 2176, 400),
        Screen(1916, 2160, 420)
    )

    private val carriers = listOf(
        Carrier("中国移动", "46000", "CN"),
        Carrier("中国联通", "46001", "CN"),
        Carrier("中国电信", "46003", "CN"),
        Carrier("中国广电", "46015", "CN")
    )

    private val timezones = listOf(
        "Asia/Shanghai", "Asia/Hong_Kong", "Asia/Taipei", "Asia/Singapore",
        "America/New_York", "Europe/London", "Asia/Tokyo"
    )

    private val languages = listOf("zh-CN", "zh-TW", "en-US", "en-GB", "ja-JP")

    fun generate(context: Context): TwigConfig.Config = generateWithName(context).first

    /**
     * 同时返回机型展示名，便于模板命名。
     */
    fun generateWithName(context: Context): Pair<TwigConfig.Config, String> {
        val device = randomDevice(context)
        val brand = device?.brand.orEmpty()
        val modelName = device?.modelName?.takeIf { it.isNotBlank() }
            ?: device?.model?.takeIf { it.isNotBlank() }
            ?: "Xiaomi 18 Pro Max"
        val model = device?.model?.takeIf { it.isNotBlank() } ?: "2611FPNFAG"
        val deviceToken = sanitize(
            device?.codeAlias?.takeIf { it.isNotBlank() }
                ?: device?.code?.takeIf { it.isNotBlank() }
                ?: model
        )
        val product = deviceToken
        val manufacturer = manufacturerOf(brand)
        val isFold = modelName.contains("Fold", true) || modelName.contains("Flip", true)
        val screen = (if (isFold) foldScreens else screens).random()
        val gpu = gpuOf(brand, modelName)
        val release = listOf(16, 17).random()
        val api = if (release >= 17) 37 else 36
        val buildId = if (release >= 17) "BP2A.260901.001" else "AP3A.260601.001"
        val incremental = "OS4.0.1.0.WPBCNXM"
        val fingerprint =
            "$manufacturer/$product/$deviceToken:$release/$buildId/$incremental:user/release-keys"
        val carrier = carriers.random()
        val wifiMac = randomMac()
        val btMac = randomMac()

        val config = TwigConfig.Config(
            MANUFACTURER = manufacturer,
            MODEL = model,
            DEVICE = deviceToken,
            PRODUCT = product,
            CHARACTERISTICS = "phone",
            BOARD = boardOf(brand, isFold),
            RADIO_VERSION = "2.0.c-00020",
            RELEASE = release,
            API = api,
            FINGERPRINT = fingerprint,
            LANGUAGE = languages.random(),
            TIMEZONE = timezones.random(),
            USB_DEBUGGING = 0,
            GPU_VENDOR = gpu.vendor,
            GPU_RENDERER = gpu.renderer,
            GPU_VERSION = "OpenGL ES 3.2 V@0615.0 (GIT@${randomHex(9)}, I${randomHex(10)}) (Date:09/18/26)",
            GPU_EXTENSIONS = "GL_EXT_debug_marker GL_ARM_rgba8 GL_ARM_mali_shader_binary GL_OES_depth24 " +
                    "GL_OES_depth_texture GL_OES_EGL_image GL_OES_EGL_image_external " +
                    "GL_OES_packed_depth_stencil GL_OES_rgb8_rgba8 GL_OES_standard_derivatives " +
                    "GL_OES_texture_float GL_OES_texture_half_float GL_KHR_debug",
            DISPLAY_WIDTH = screen.width,
            DISPLAY_HEIGHT = screen.height,
            DISPLAY_DENSITY_DPI = screen.dpi,
            DISPLAY_FONT_SCALE = 1.0F,
            NETWORK_TYPE = 1,
            MOBILE_NETWORK_TYPE = 20,
            WIFI_SSID = "${modelName.replace(" ", "_")}_${randomHex(4).uppercase()}",
            WIFI_BSSID = wifiMac,
            WIFI_MAC = wifiMac,
            BLUETOOTH_NAME = modelName,
            BLUETOOTH_MAC = btMac,
            SIM_OPERATOR = carrier.code,
            SIM_OPERATOR_NAME = carrier.name,
            SIM_COUNTRY_ISO = carrier.country,
            SIM_STATE = 5,
            SIM_SERIAL_NUMBER = "8986${randomDigits(16)}",
            LAC = Random.nextInt(1, 65535),
            CID = Random.nextInt(1, 268435455),
            IMEI = randomImei(),
            DEVICE_ID = randomHex(16),
            PHONE_NUMBER = "",
            BATTERY_LEVEL = Random.nextInt(55, 98),
            ANTI_DETECTION = false,
            ANTI_FINGERPRINT = false
        )
        return config to modelName
    }

    private fun randomDevice(context: Context): Device? = runCatching {
        DeviceDBHelper(context).use { it.getRandomDevice(minYear = 2026) }
    }.getOrNull()

    private fun manufacturerOf(brand: String): String = when (brand.lowercase()) {
        "xiaomi", "redmi", "poco" -> "Xiaomi"
        "samsung" -> "samsung"
        "google" -> "Google"
        "apple" -> "Apple"
        "huawei" -> "HUAWEI"
        "honor" -> "HONOR"
        "oppo" -> "OPPO"
        "vivo", "iqoo" -> "vivo"
        "oneplus" -> "OnePlus"
        "realme" -> "realme"
        "motorola" -> "motorola"
        "nokia" -> "Nokia"
        "sony" -> "Sony"
        "asus" -> "ASUS"
        "zte" -> "ZTE"
        "nubia" -> "nubia"
        "lenovo" -> "Lenovo"
        "meizu" -> "Meizu"
        "nothing" -> "Nothing"
        else -> brand.ifBlank { "Xiaomi" }
    }

    private fun boardOf(brand: String, isFold: Boolean): String = when {
        isFold -> "o3"
        brand.equals("samsung", true) -> "dm3q2q"
        brand.equals("google", true) -> "zumapro"
        brand.equals("huawei", true) || brand.equals("honor", true) -> "hi36a0"
        brand.equals("Xiaomi", true) -> "sun"
        else -> "taro"
    }

    private fun gpuOf(brand: String, modelName: String): Gpu = when {
        brand.equals("Apple", true) -> gpus.first { it.renderer == "Apple GPU" }
        brand.equals("samsung", true) -> gpus.first { it.renderer == "Xclipse 950" }
        brand.equals("huawei", true) || brand.equals("honor", true) ->
            gpus.first { it.renderer == "Maleoon 920" }
        brand.equals("google", true) -> gpus.first { it.renderer == "Mali-G715-Immortalis MC11" }
        modelName.contains("Dimensity", true) || modelName.contains("XRing", true) ->
            gpus[Random.nextInt(3, 6)]
        else -> gpus[Random.nextInt(0, 3)]
    }

    private fun sanitize(value: String): String = value
        .replace(Regex("[^A-Za-z0-9]"), "_")
        .trim('_')
        .lowercase()
        .ifBlank { "device" }
        .take(24)

    private fun randomHex(length: Int): String {
        val chars = "0123456789abcdef"
        return (1..length).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }

    private fun randomDigits(length: Int): String =
        (1..length).map { Random.nextInt(0, 10) }.joinToString("")

    private fun randomMac(): String {
        // 本地管理地址，避免与真实厂商标识冲突
        val first = (Random.nextInt(0x00, 0x100) and 0xFC) or 0x02
        val bytes = mutableListOf(first)
        repeat(5) { bytes.add(Random.nextInt(0, 256)) }
        return bytes.joinToString(":") { "%02x".format(it) }
    }

    /**
     * 生成通过 Luhn 校验的 15 位 IMEI。
     */
    private fun randomImei(): String {
        val body = "35" + randomDigits(12)
        return body + luhnCheckDigit(body)
    }

    private fun luhnCheckDigit(body: String): Char {
        var sum = 0
        var double = true
        for (i in body.indices.reversed()) {
            var digit = body[i] - '0'
            if (double) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
            double = !double
        }
        return ((10 - sum % 10) % 10).digitToChar()
    }
}
