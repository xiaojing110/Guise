package com.houvven.twig.config

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.houvven.androidc.application.ApplicationUtils
import com.houvven.androidc.command.ShellUtils
import com.houvven.twig.TwigStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json


/**
 * Twig相关配置密封类
 */
@Serializable
@Suppress("unused", "PropertyName")
sealed interface TwigConfig {

    companion object {
        const val DEFAULT_INT = -99
        const val DEFAULT_STRING = ""
        const val DEFAULT_DOUBLE = -99.0
        const val DEFAULT_FLOAT = -99.0F
    }

    // 设备信息
    val MANUFACTURER: Any
    val MODEL: Any
    val DEVICE: Any
    val PRODUCT: Any
    val CHARACTERISTICS: Any
    val BOARD: Any
    val RADIO_VERSION: Any

    // 系统信息
    val RELEASE: Any
    val API: Any
    val FINGERPRINT: Any
    val LANGUAGE: Any
    val TIMEZONE: Any
    val USB_DEBUGGING: Any

    // 硬件信息
    val GPU_VENDOR: Any
    val GPU_RENDERER: Any
    val GPU_VERSION: Any
    val GPU_EXTENSIONS: Any
    val DISPLAY_WIDTH: Any
    val DISPLAY_HEIGHT: Any
    val DISPLAY_DENSITY_DPI: Any
    val DISPLAY_FONT_SCALE: Any

    // 网络信息
    val NETWORK_TYPE: Any
    val MOBILE_NETWORK_TYPE: Any
    val WIFI_SSID: Any
    val WIFI_BSSID: Any
    val WIFI_MAC: Any
    val BLUETOOTH_NAME: Any
    val BLUETOOTH_MAC: Any

    // SIM信息
    val SIM_OPERATOR: Any
    val SIM_OPERATOR_NAME: Any
    val SIM_COUNTRY_ISO: Any
    val SIM_STATE: Any
    val SIM_SERIAL_NUMBER: Any
    val LAC: Any
    val CID: Any

    // 位置信息
    val LATITUDE: Any
    val LONGITUDE: Any
    val ALTITUDE: Any
    val DISABLE_WIFI_LOCATION: Any
    val DISABLE_TEL_LOCATION: Any

    // 唯一标识
    val IMEI: Any
    val DEVICE_ID: Any
    val PHONE_NUMBER: Any

    // App信息
    val APP_VERSION_NAME: Any
    val APP_VERSION_CODE: Any
    val APP_BUILD_TYPE: Any

    // 其他
    val BATTERY_LEVEL: Any
    val BATTERY_CHARGING: Any
    val SCREENSHOTS: Any

    // 隐私
    val BLANK_PASS_PHOTO: Any
    val BLANK_PASS_VIDEO: Any
    val BLANK_PASS_AUDIO: Any
    val BLANK_PASS_CONTACTS: Any

    @Serializable
    data class Config(
        @Transient
        val packageName: String = "",
        override val MANUFACTURER: String = DEFAULT_STRING,
        override val MODEL: String = DEFAULT_STRING,
        override val DEVICE: String = DEFAULT_STRING,
        override val PRODUCT: String = DEFAULT_STRING,
        override val CHARACTERISTICS: String = DEFAULT_STRING,
        override val BOARD: String = DEFAULT_STRING,
        override val RADIO_VERSION: String = DEFAULT_STRING,
        override val RELEASE: Int = DEFAULT_INT,
        override val API: Int = DEFAULT_INT,
        override val FINGERPRINT: String = DEFAULT_STRING,
        override val LANGUAGE: String = DEFAULT_STRING,
        override val TIMEZONE: String = DEFAULT_STRING,
        override val USB_DEBUGGING: Int = DEFAULT_INT,
        override val GPU_VENDOR: String = DEFAULT_STRING,
        override val GPU_RENDERER: String = DEFAULT_STRING,
        override val GPU_VERSION: String = DEFAULT_STRING,
        override val GPU_EXTENSIONS: String = DEFAULT_STRING,
        override val DISPLAY_WIDTH: Int = DEFAULT_INT,
        override val DISPLAY_HEIGHT: Int = DEFAULT_INT,
        override val DISPLAY_DENSITY_DPI: Int = DEFAULT_INT,
        override val DISPLAY_FONT_SCALE: Float = DEFAULT_FLOAT,
        override val NETWORK_TYPE: Int = DEFAULT_INT,
        override val MOBILE_NETWORK_TYPE: Int = DEFAULT_INT,
        override val WIFI_SSID: String = DEFAULT_STRING,
        override val WIFI_BSSID: String = DEFAULT_STRING,
        override val WIFI_MAC: String = DEFAULT_STRING,
        override val BLUETOOTH_NAME: String = DEFAULT_STRING,
        override val BLUETOOTH_MAC: String = DEFAULT_STRING,
        override val SIM_OPERATOR: String = DEFAULT_STRING,
        override val SIM_OPERATOR_NAME: String = DEFAULT_STRING,
        override val SIM_COUNTRY_ISO: String = DEFAULT_STRING,
        override val SIM_STATE: Int = DEFAULT_INT,
        override val SIM_SERIAL_NUMBER: String = DEFAULT_STRING,
        override val LAC: Int = DEFAULT_INT,
        override val CID: Int = DEFAULT_INT,
        override val LATITUDE: Double = DEFAULT_DOUBLE,
        override val LONGITUDE: Double = DEFAULT_DOUBLE,
        override val ALTITUDE: Double = DEFAULT_DOUBLE,
        override val DISABLE_WIFI_LOCATION: Boolean = false,
        override val DISABLE_TEL_LOCATION: Boolean = false,
        override val IMEI: String = DEFAULT_STRING,
        override val DEVICE_ID: String = DEFAULT_STRING,
        override val PHONE_NUMBER: String = DEFAULT_STRING,
        override val APP_VERSION_NAME: String = DEFAULT_STRING,
        override val APP_VERSION_CODE: Int = DEFAULT_INT,
        override val APP_BUILD_TYPE: Int = DEFAULT_INT,
        override val BATTERY_LEVEL: Int = DEFAULT_INT,
        override val BATTERY_CHARGING: Boolean = false,
        override val SCREENSHOTS: Int = DEFAULT_INT,
        override val BLANK_PASS_PHOTO: Boolean = false,
        override val BLANK_PASS_VIDEO: Boolean = false,
        override val BLANK_PASS_AUDIO: Boolean = false,
        override val BLANK_PASS_CONTACTS: Boolean = false,
    ) : TwigConfig {
        @Transient
        val isEnable: Boolean get() = this != default.copy(packageName = packageName)
        fun toJSON() = Json.encodeToString(serializer(), this)

        companion object {
            val default = Config()

            fun fromJSON(json: String) = Json.decodeFromString(this.serializer(), json)
        }
    }

    class State private constructor(config: Config) : TwigConfig {
        override lateinit var MANUFACTURER: MutableState<String>
        override lateinit var MODEL: MutableState<String>
        override lateinit var DEVICE: MutableState<String>
        override lateinit var PRODUCT: MutableState<String>
        override lateinit var CHARACTERISTICS: MutableState<String>
        override lateinit var BOARD: MutableState<String>
        override lateinit var RADIO_VERSION: MutableState<String>
        override lateinit var RELEASE: MutableState<String>
        override lateinit var API: MutableState<String>
        override lateinit var FINGERPRINT: MutableState<String>
        override lateinit var LANGUAGE: MutableState<String>
        override lateinit var TIMEZONE: MutableState<String>
        override lateinit var USB_DEBUGGING: MutableState<String>
        override lateinit var GPU_VENDOR: MutableState<String>
        override lateinit var GPU_RENDERER: MutableState<String>
        override lateinit var GPU_VERSION: MutableState<String>
        override lateinit var GPU_EXTENSIONS: MutableState<String>
        override lateinit var DISPLAY_WIDTH: MutableState<String>
        override lateinit var DISPLAY_HEIGHT: MutableState<String>
        override lateinit var DISPLAY_DENSITY_DPI: MutableState<String>
        override lateinit var DISPLAY_FONT_SCALE: MutableState<String>
        override lateinit var NETWORK_TYPE: MutableState<String>
        override lateinit var MOBILE_NETWORK_TYPE: MutableState<String>
        override lateinit var WIFI_SSID: MutableState<String>
        override lateinit var WIFI_BSSID: MutableState<String>
        override lateinit var WIFI_MAC: MutableState<String>
        override lateinit var BLUETOOTH_NAME: MutableState<String>
        override lateinit var BLUETOOTH_MAC: MutableState<String>
        override lateinit var SIM_OPERATOR: MutableState<String>
        override lateinit var SIM_OPERATOR_NAME: MutableState<String>
        override lateinit var SIM_COUNTRY_ISO: MutableState<String>
        override lateinit var SIM_STATE: MutableState<String>
        override lateinit var SIM_SERIAL_NUMBER: MutableState<String>
        override lateinit var LAC: MutableState<String>
        override lateinit var CID: MutableState<String>
        override lateinit var LATITUDE: MutableState<String>
        override lateinit var LONGITUDE: MutableState<String>
        override lateinit var ALTITUDE: MutableState<String>
        override lateinit var DISABLE_WIFI_LOCATION: MutableState<Boolean>
        override lateinit var DISABLE_TEL_LOCATION: MutableState<Boolean>
        override lateinit var IMEI: MutableState<String>
        override lateinit var DEVICE_ID: MutableState<String>
        override lateinit var PHONE_NUMBER: MutableState<String>
        override lateinit var APP_VERSION_NAME: MutableState<String>
        override lateinit var APP_VERSION_CODE: MutableState<String>
        override lateinit var APP_BUILD_TYPE: MutableState<String>
        override lateinit var BATTERY_LEVEL: MutableState<String>
        override lateinit var BATTERY_CHARGING: MutableState<Boolean>
        override lateinit var SCREENSHOTS: MutableState<String>
        override lateinit var BLANK_PASS_PHOTO: MutableState<Boolean>
        override lateinit var BLANK_PASS_VIDEO: MutableState<Boolean>
        override lateinit var BLANK_PASS_AUDIO: MutableState<Boolean>
        override lateinit var BLANK_PASS_CONTACTS: MutableState<Boolean>


        init {
            val configFields = Config::class.java.declaredFields
            val stateFields = this::class.java.declaredFields
                .filter { it.type == MutableState::class.java }
                .toMutableList()

            configFields.forEach { configField ->
                val stateField = stateFields.find { it.name == configField.name } ?: return@forEach
                configField.isAccessible = true
                val defaultValue = configField.get(Config.default)
                when (val value = configField.get(config)) {
                    is String -> stateField.set(this, mutableStateOf(value))
                    is Boolean -> stateField.set(this, mutableStateOf(value))
                    else -> stateField.set(
                        this,
                        mutableStateOf(if (value == defaultValue) DEFAULT_STRING else value.toString())
                    )
                }
                stateFields.remove(stateField)
            }
        }

        companion object {
            fun of(config: Config) = State(config)
        }
    }

    class Manager(val config: Config) {
        var state: State = State.of(config)
            private set

        infix fun setState(state: State) {
            this.state = state
        }

        fun stopApp(context: Context, packageName: String) {
            save()
            if (ShellUtils.checkRoot()) {
                ApplicationUtils.stopWithRoot(packageName)
            } else {
                ApplicationUtils.stop(context, packageName)
            }
        }

        fun restartApp(context: Context, packageName: String) {
            save()
            if (ShellUtils.checkRoot()) {
                ApplicationUtils.restartWithRoot(context, packageName)
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun clear() {
            val stateFields = State::class.java.declaredFields
                .filter { it.type == MutableState::class.java }

            stateFields.forEach {
                val state = (it.get(state) as MutableState<*>)
                if (state.value is Boolean) (state as MutableState<Boolean>).value = false
                else (state as MutableState<String>).value = ""
            }
        }

        fun save() {
            this.updateConfigFromState()
            TwigStore.APP.save(config)
        }

        private fun updateConfigFromState() {
            val configFields = config.javaClass.declaredFields.toMutableList()
            val stateFields = state.javaClass.declaredFields
                .filter { it.type == MutableState::class.java }
                .toMutableList()

            configFields.forEach { configField ->
                val stateField =
                    stateFields.find { stateField -> configField.name == stateField.name }
                        ?: return@forEach
                val value = (stateField.get(state) as MutableState<*>).value
                configField.isAccessible = true
                // configFields.remove(configField)
                stateFields.remove(stateField)
                if (configField.type == Boolean::class.java) {
                    configField.setBoolean(config, value as Boolean)
                    return@forEach
                } else if (configField.type == String::class.java) {
                    configField.set(config, value as String)
                    return@forEach
                }

                value as String
                when (configField.type) {
                    Int::class.java -> (value.toIntOrNull() ?: configField.getInt(Config.default))
                        .let { configField.setInt(config, it) }

                    Long::class.java -> (value.toLongOrNull() ?: configField.getLong(Config.default))
                        .let { configField.setLong(config, it) }

                    Short::class.java -> (value.toShortOrNull() ?: configField.getShort(Config.default))
                        .let { configField.setShort(config, it) }

                    Byte::class.java -> (value.toByteOrNull() ?: configField.getByte(Config.default))
                        .let { configField.setByte(config, it) }

                    Double::class.java -> (value.toDoubleOrNull() ?: configField.getDouble(Config.default))
                        .let { configField.setDouble(config, it) }

                    Float::class.java -> (value.toFloatOrNull() ?: configField.getFloat(Config.default))
                        .let { configField.setFloat(config, it) }

                    Char::class.java -> (value.singleOrNull() ?: configField.getChar(Config.default))
                        .let { configField.setChar(config, it) }

                    else -> Unit
                }
                if (configFields.isEmpty() || stateFields.isEmpty()) return
            }
        }
    }
}