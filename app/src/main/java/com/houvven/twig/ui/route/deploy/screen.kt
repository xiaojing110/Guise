package com.houvven.twig.ui.route.deploy

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.houvven.androidc.information.DisplayInfoProvider
import com.houvven.androidc.utils.SystemProperties
import com.houvven.twig.R
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.preset.Presets
import com.houvven.twig.preset.adapter.PresetAdapter
import com.houvven.twig.preset.device.DevicePreset
import com.houvven.twig.ui.components.layout.ScrollColumn
import com.houvven.twig.ui.route.Routes
import com.houvven.twig.ui.route.screen.MapPointSelectionParams
import com.houvven.twig.ui.utils.antiShakeNavOption
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.TimeZone


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun DeployScreen(
    manager: TwigConfig.Manager,
    topAppBar: @Composable TwigConfig.Manager.() -> Unit
) {
    // val sheetState = rememberBottomSheetScaffoldState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    if (!LocalConfigState.isBackFromMapPointSelection) {
        LocalConfigState.currentStateCache.value = manager.state
    } else {
        manager setState LocalConfigState.currentStateCache.value
        LocalConfigState.isBackFromMapPointSelection = false
    }

    @Composable
    fun PresetView(presets: List<PresetAdapter>) = LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 15.dp),
    ) {
        items(presets) {
            ElevatedSuggestionChip(
                onClick = {
                    LocalConfigState.setValue(it.value)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) openBottomSheet = false
                    }
                },
                label = { Row(Modifier.padding(vertical = 15.dp)) { Text(it.label) } },
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
            )
        }
    }

    val presets = LocalConfigState.preset.value

    Scaffold(
        topBar = { manager.topAppBar() },
    ) {
        ScrollColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConfigurationItem {
                openBottomSheet = !openBottomSheet
            }
        }

        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(0.95F)
                    .padding(top = it.calculateTopPadding()),
                shape = MaterialTheme.shapes.large
            ) {
                PresetView(presets)
            }
        }
    }


    BackHandler(
        enabled = sheetState.isVisible,
        onBack = { scope.launch { sheetState.hide() } }
    )

}


@Composable
private fun ItemContainer(title: @Composable () -> String, content: @Composable () -> Unit) {
    val shape = MaterialTheme.shapes.medium
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(shape = shape),
    ) {
        Text(
            text = title(),
            modifier = Modifier.padding(start = 20.dp, top = 15.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
            content()
        }
    }
}

@Composable
private fun ConfigurationItem(launch: () -> Unit) = LocalConfigState.currentStateCache.value.run {
    @Composable
    fun PresetInput(
        state: MutableState<String>,
        label: @Composable () -> String,
        placeholder: (() -> String)? = null,
        preset: List<PresetAdapter>,
        showOperateIcon: Boolean = true,
        set: (String) -> Unit = { value -> state.value = value },
    ) = OperateInputBox(
        state = state,
        label = label,
        placeholder = placeholder,
        showOperateIcon = showOperateIcon
    ) {
        LocalConfigState.preset.value = preset
        LocalConfigState.setValueMethod.value = set
        launch()
    }

    val context = LocalContext.current
    val displayInfo = DisplayInfoProvider.of(context)

    ItemContainer({ stringResource(id = R.string.deploy_title_device_info) }) {
        PresetInput(
            state = MANUFACTURER,
            label = { stringResource(id = R.string.deploy_device_info_manufacturer) },
            placeholder = { Build.MANUFACTURER },
            preset = Presets.MANUFACTURER
        )
        PresetInput(
            state = MODEL,
            label = { stringResource(id = R.string.deploy_device_info_model) },
            placeholder = { Build.MODEL },
            preset = DevicePreset.getModelsPreset(MANUFACTURER.value),
            showOperateIcon = DevicePreset.allBrands.containsKey(MANUFACTURER.value),
            set = {
                val (model, codeAlias) = it.split(":")
                MODEL.value = model
                DEVICE.value = codeAlias
            }
        )
        Input(state = DEVICE, label = { "Device" }, placeholder = { Build.DEVICE })
        Input(state = PRODUCT, label = { "Product" }, placeholder = { Build.PRODUCT })
        DropMenuInput(
            state = CHARACTERISTICS,
            label = { "CHARACTERISTICS" },
            placeholder = { SystemProperties.get("ro.build.characteristics", "") },
            preset = Presets.CHARACTERISTICS
        )
        Input(state = BOARD, label = { "Board" }, placeholder = { Build.BOARD })
        Input(
            state = RADIO_VERSION,
            label = { "Radio Version" },
            placeholder = { Build.getRadioVersion() }
        )
    }
    ItemContainer({ stringResource(id = R.string.deploy_title_system_info) }) {
        PresetInput(
            state = RELEASE,
            label = { stringResource(id = R.string.deploy_system_info_release) },
            placeholder = { Build.VERSION.RELEASE },
            preset = Presets.RELEASE
        )
        PresetInput(
            state = API,
            label = { "API" },
            placeholder = { Build.VERSION.SDK_INT.toString() },
            preset = Presets.SDK_INT
        )
        Input(state = FINGERPRINT, label = { "Fingerprint" }, placeholder = { Build.FINGERPRINT })
        PresetInput(
            state = LANGUAGE,
            label = { stringResource(id = R.string.deploy_system_info_language) },
            placeholder = { Locale.getDefault().toString() },
            preset = Presets.LANGUAGE
        )
        PresetInput(
            state = TIMEZONE,
            label = { stringResource(id = R.string.deploy_system_info_timezone) },
            placeholder = { TimeZone.getDefault().id },
            preset = Presets.TIMEZONE
        )
        DropMenuInput(
            state = USB_DEBUGGING,
            label = { stringResource(id = R.string.deploy_system_info_usb_debbuging) },
            preset = Presets.USB_DEBUGGING
        )
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_hardware_info) }) {
        Input(
            state = GPU_VENDOR,
            label = { stringResource(id = R.string.deploy_hardware_info_gpu_vendor) },
        )
        Input(
            state = GPU_RENDERER,
            label = { stringResource(id = R.string.deploy_hardware_info_gpu_renderer) },
        )
        Input(
            state = GPU_VERSION,
            label = { stringResource(id = R.string.deploy_hardware_info_gpu_version) },
        )
        Input(
            state = GPU_EXTENSIONS,
            label = { stringResource(id = R.string.deploy_hardware_info_gpu_extensions) },
        )
        Input(
            state = DISPLAY_WIDTH,
            label = { stringResource(id = R.string.deploy_hardware_info_display_width) },
            placeholder = { displayInfo.realWidth.toString() }
        )
        Input(
            state = DISPLAY_HEIGHT,
            label = { stringResource(id = R.string.deploy_hardware_info_display_height) },
            placeholder = { displayInfo.realHeight.toString() }
        )
        Input(
            state = DISPLAY_DENSITY_DPI,
            label = { stringResource(id = R.string.deploy_hardware_info_display_density_dpi) },
            placeholder = { displayInfo.realDensityDpi.toString() }
        )
        Input(
            state = DISPLAY_FONT_SCALE,
            label = { stringResource(id = R.string.deploy_hardware_info_display_font_scale) },
            placeholder = { context.resources.configuration.fontScale.toString() }
        )
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_network_info) }) {
        DropMenuInput(
            state = NETWORK_TYPE,
            label = { stringResource(id = R.string.deploy_network_info_network_type) },
            preset = Presets.NETWORK_TYPE
        )
        PresetInput(
            state = MOBILE_NETWORK_TYPE,
            label = { stringResource(id = R.string.deploy_network_info_mobile_network_type) },
            preset = Presets.MOBILE_NETWORK_TYPE
        )
        Input(
            state = WIFI_SSID,
            label = { stringResource(id = R.string.deploy_network_info_wifi_ssid) })
        Input(
            state = WIFI_BSSID,
            label = { stringResource(id = R.string.deploy_network_info_wifi_bssid) })
        Input(
            state = WIFI_MAC,
            label = { stringResource(id = R.string.deploy_network_info_wifi_mac) })
        Input(
            state = BLUETOOTH_NAME,
            label = { stringResource(id = R.string.deploy_network_info_bluetooth_name) })
        Input(
            state = BLUETOOTH_MAC,
            label = { stringResource(id = R.string.deploy_network_info_bluetooth_mac) })
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_sim_info) }) {
        PresetInput(
            state = SIM_OPERATOR,
            label = { stringResource(id = R.string.sim_info_operator) },
            preset = Presets.SIM,
            set = {
                val (name, code, country) = it.split(":")
                SIM_OPERATOR.value = code
                SIM_OPERATOR_NAME.value = name
                SIM_COUNTRY_ISO.value = country
            }
        )
        Input(
            state = SIM_OPERATOR_NAME,
            label = { stringResource(id = R.string.sim_info_operator_name) })
        Input(
            state = SIM_COUNTRY_ISO,
            label = { stringResource(id = R.string.sim_info_operator_country) })
        Input(state = LAC, label = { "Lac" })
        Input(state = CID, label = { "Cid" })
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_location_info) }) {
        OperateInputBox(
            state = LONGITUDE,
            label = { stringResource(id = R.string.deploy_location_info_longitude) },
            clickable = {
                LocalConfigState.isBackFromMapPointSelection = true
                antiShakeNavOption(Routes.MAP_POINT_SELECTION.name)
                MapPointSelectionParams.setOnLeaving { point ->
                    LONGITUDE.value = point.lng.toString()
                    LATITUDE.value = point.lat.toString()
                }
            }
        )
        Input(
            state = LATITUDE,
            label = { stringResource(id = R.string.deploy_location_info_latitude) }
        )
        Input(
            state = ALTITUDE,
            label = { stringResource(id = R.string.deploy_location_info_altitude) }
        )
        SwitchInput(
            state = DISABLE_WIFI_LOCATION,
            label = { stringResource(id = R.string.deploy_location_info_disable_wifi_location) }
        )
        SwitchInput(
            state = DISABLE_TEL_LOCATION,
            label = { stringResource(id = R.string.deploy_location_info_disable_tel_location) }
        )
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_app_info) }) {
        Input(state = APP_VERSION_NAME, label = { stringResource(id = R.string.deploy_app_version_name) })
        Input(state = APP_VERSION_CODE, label = { stringResource(id = R.string.deploy_app_version_code) })
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_identifies) }) {
        Input(state = IMEI, label = { "IMEI" })
        Input(state = DEVICE_ID, label = { stringResource(id = R.string.deploy_device_id) })
        Input(state = PHONE_NUMBER, label = { stringResource(id = R.string.deploy_phone_number) })
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_other) }) {
        Input(state = BATTERY_LEVEL, label = { stringResource(id = R.string.deploy_other_battery_level) })
        DropMenuInput(state = SCREENSHOTS, label = { stringResource(id = R.string.deploy_other_screenshots) }, preset = Presets.SCREENSHOTS)
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_blank_pass) }) {
        SwitchInput(state = BLANK_PASS_PHOTO, label = { stringResource(id = R.string.deploy_blank_pass_photo) })
        SwitchInput(state = BLANK_PASS_VIDEO, label = { stringResource(id = R.string.deploy_blank_pass_video) })
        SwitchInput(state = BLANK_PASS_AUDIO, label = { stringResource(id = R.string.deploy_blank_pass_audio) })
        SwitchInput(state = BLANK_PASS_CONTACTS, label = { stringResource(id = R.string.deploy_blank_pass_contacts) })
    }
    ItemContainer(title = { stringResource(id = R.string.deploy_title_disable_sensor) }) {

    }
}