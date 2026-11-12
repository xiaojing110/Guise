package com.houvven.twig.config

import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class ConfigTemplate(
    val id: String,
    val name: String,
    val description: String = "",
    val builtIn: Boolean = false,
    val updatedAt: Long = 0L,
    val config: TwigConfig.Config = TwigConfig.Config()
) {
    val summary: String
        get() {
            val device = listOf(config.MANUFACTURER, config.MODEL)
                .filter { it.isNotBlank() }
                .joinToString(" ")
            val android = if (config.RELEASE > 0) "Android ${config.RELEASE}" else ""
            return listOf(device, android).filter { it.isNotBlank() }.joinToString(" · ")
        }
}

/**
 * 模板仓库。
 *
 * - 内置模板始终存在且不可删除（小米 18 Pro Max / 小米 18 Fold）
 * - 用户模板保存在 MMKV，保存后立即持久化，重启应用不会丢失
 */
@Suppress("unused")
object TemplateStore {

    private const val STORE_KEY = "config_templates_v1"

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val serializer = ListSerializer(ConfigTemplate.serializer())

    private val _templates = mutableStateOf<List<ConfigTemplate>>(emptyList())

    val templates: List<ConfigTemplate> get() {
        ensureLoaded()
        return _templates.value
    }

    private var loaded = false

    @Synchronized
    private fun ensureLoaded() {
        if (loaded) return
        loaded = true
        val saved = runCatching {
            val raw = AppConfig.run { mmkv<String> { decodeString(STORE_KEY, "")!! } }
            if (raw.isBlank()) emptyList()
            else json.decodeFromString(serializer, raw)
        }.getOrDefault(emptyList())
        _templates.value = BuiltInTemplates.all + saved
            .filterNot { it.builtIn || it.id.startsWith("builtin_") }
            .distinctBy { it.id }
    }

    fun find(id: String): ConfigTemplate? = templates.find { it.id == id }

    /**
     * 保存当前配置为模板。同名用户模板会被覆盖，避免重复堆积。
     */
    fun save(name: String, config: TwigConfig.Config, description: String = ""): ConfigTemplate {
        ensureLoaded()
        val requestedName = name.trim().ifBlank { "未命名模板" }
        val safeName = if (BuiltInTemplates.all.any { it.name == requestedName }) {
            "$requestedName (自定义)"
        } else requestedName
        val old = _templates.value.find { !it.builtIn && it.name == safeName }
        val template = ConfigTemplate(
            id = old?.id ?: "user_${UUID.randomUUID()}",
            name = safeName,
            description = description,
            builtIn = false,
            updatedAt = System.currentTimeMillis(),
            config = config.copy(packageName = "")
        )
        _templates.value = if (old == null) {
            listOf(template) + _templates.value
        } else {
            _templates.value.map { if (it.id == old.id) template else it }
        }
        persist()
        return template
    }

    fun delete(id: String): Boolean {
        ensureLoaded()
        val target = _templates.value.find { it.id == id } ?: return false
        if (target.builtIn) return false
        _templates.value = _templates.value.filterNot { it.id == id }
        persist()
        return true
    }

    private fun persist() {
        val userTemplates = _templates.value.filterNot { it.builtIn }
        runCatching {
            val raw = json.encodeToString(serializer, userTemplates)
            AppConfig.run { mmkv { encode(STORE_KEY, raw) } }
        }
    }
}

/**
 * 内置模板。
 */
private object BuiltInTemplates {

    val all: List<ConfigTemplate>
        get() = listOf(xiaomi18ProMax, xiaomi18Fold)

    val xiaomi18ProMax = ConfigTemplate(
        id = "builtin_xiaomi_18_pro_max",
        name = "小米18 Pro Max",
        description = "Xiaomi 18 Pro Max · Snapdragon 8 Elite Extreme Gen 6 · Android 17 / HyperOS 4",
        builtIn = true,
        updatedAt = 1_791_000_000_000L,
        config = TwigConfig.Config(
            MANUFACTURER = "Xiaomi",
            MODEL = "2611FPNFAG",
            DEVICE = "2611FPNFAG",
            PRODUCT = "2611FPNFAG",
            CHARACTERISTICS = "phone",
            BOARD = "sun",
            RADIO_VERSION = "2.0.c-00020",
            RELEASE = 17,
            API = 37,
            FINGERPRINT = "Xiaomi/2611FPNFAG/2611FPNFAG:17/BP2A.260901.001/OS4.0.1.0.WPBCNXM:user/release-keys",
            LANGUAGE = "zh-CN",
            TIMEZONE = "Asia/Shanghai",
            USB_DEBUGGING = 0,
            GPU_VENDOR = "Qualcomm",
            GPU_RENDERER = "Adreno (TM) 850",
            GPU_VERSION = "OpenGL ES 3.2 V@0615.0 (GIT@b1a2c3d, I46f5e2a1b9) (Date:09/18/26)",
            GPU_EXTENSIONS = "GL_EXT_debug_marker GL_ARM_rgba8 GL_ARM_mali_shader_binary GL_OES_depth24 " +
                    "GL_OES_depth_texture GL_OES_EGL_image GL_OES_EGL_image_external GL_OES_packed_depth_stencil " +
                    "GL_OES_rgb8_rgba8 GL_OES_standard_derivatives GL_OES_texture_float GL_OES_texture_half_float " +
                    "GL_OES_vertex_half_float GL_KHR_blend_equation_advanced GL_KHR_debug GL_KHR_texture_compression_astc_ldr",
            DISPLAY_WIDTH = 1208,
            DISPLAY_HEIGHT = 2624,
            DISPLAY_DENSITY_DPI = 480,
            DISPLAY_FONT_SCALE = 1.0F,
            NETWORK_TYPE = 1,
            MOBILE_NETWORK_TYPE = 20,
            WIFI_SSID = "Xiaomi_18ProMax",
            WIFI_BSSID = "3c:5a:b4:8e:21:f7",
            WIFI_MAC = "3c:5a:b4:8e:21:f7",
            BLUETOOTH_NAME = "Xiaomi 18 Pro Max",
            BLUETOOTH_MAC = "3c:5a:b4:8e:22:08",
            SIM_OPERATOR = "46000",
            SIM_OPERATOR_NAME = "中国移动",
            SIM_COUNTRY_ISO = "CN",
            SIM_STATE = 5,
            SIM_SERIAL_NUMBER = "89860012345678901234",
            LAC = 2468,
            CID = 13579,
            IMEI = "867543210123452",
            DEVICE_ID = "a1b2c3d4e5f60718",
            PHONE_NUMBER = "",
            BATTERY_LEVEL = 86,
            ANTI_DETECTION = true,
            ANTI_FINGERPRINT = true
        )
    )

    val xiaomi18Fold = ConfigTemplate(
        id = "builtin_xiaomi_18_fold",
        name = "小米18 Fold",
        description = "Xiaomi 18 Fold · XRing O3 · Android 17 / HyperOS 4 · 7.58\" 折叠屏",
        builtIn = true,
        updatedAt = 1_791_000_000_000L,
        config = TwigConfig.Config(
            MANUFACTURER = "Xiaomi",
            MODEL = "2608BPX34C",
            DEVICE = "2608BPX34C",
            PRODUCT = "2608BPX34C",
            CHARACTERISTICS = "phone",
            BOARD = "o3",
            RADIO_VERSION = "2.0.c-00020",
            RELEASE = 17,
            API = 37,
            FINGERPRINT = "Xiaomi/2608BPX34C/2608BPX34C:17/BP2A.260901.001/OS4.0.1.0.WPBCNXM:user/release-keys",
            LANGUAGE = "zh-CN",
            TIMEZONE = "Asia/Shanghai",
            USB_DEBUGGING = 0,
            GPU_VENDOR = "ARM",
            GPU_RENDERER = "Arm G2-Ultra NX MC16",
            GPU_VERSION = "OpenGL ES 3.2 V@0615.0 (GIT@a9f3c1d, I7b6e4a2c8f) (Date:09/10/26)",
            GPU_EXTENSIONS = "GL_EXT_debug_marker GL_ARM_rgba8 GL_ARM_mali_shader_binary GL_OES_depth24 " +
                    "GL_OES_depth_texture GL_OES_EGL_image GL_OES_EGL_image_external GL_OES_packed_depth_stencil " +
                    "GL_OES_rgb8_rgba8 GL_OES_standard_derivatives GL_OES_texture_float GL_OES_texture_half_float " +
                    "GL_OES_vertex_half_float GL_KHR_blend_equation_advanced GL_KHR_debug GL_KHR_texture_compression_astc_ldr",
            DISPLAY_WIDTH = 1672,
            DISPLAY_HEIGHT = 2364,
            DISPLAY_DENSITY_DPI = 420,
            DISPLAY_FONT_SCALE = 1.0F,
            NETWORK_TYPE = 1,
            MOBILE_NETWORK_TYPE = 20,
            WIFI_SSID = "Xiaomi_18Fold",
            WIFI_BSSID = "3c:5a:b4:71:0a:2c",
            WIFI_MAC = "3c:5a:b4:71:0a:2c",
            BLUETOOTH_NAME = "Xiaomi 18 Fold",
            BLUETOOTH_MAC = "3c:5a:b4:71:0a:3d",
            SIM_OPERATOR = "46001",
            SIM_OPERATOR_NAME = "中国联通",
            SIM_COUNTRY_ISO = "CN",
            SIM_STATE = 5,
            SIM_SERIAL_NUMBER = "89860112345678901234",
            LAC = 1357,
            CID = 24680,
            IMEI = "867543210987658",
            DEVICE_ID = "b2c3d4e5f6071829",
            PHONE_NUMBER = "",
            BATTERY_LEVEL = 78,
            ANTI_DETECTION = true,
            ANTI_FINGERPRINT = true
        )
    )
}
