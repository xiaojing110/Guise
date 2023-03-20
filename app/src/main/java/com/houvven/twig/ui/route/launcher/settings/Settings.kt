package com.houvven.twig.ui.route.launcher.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.houvven.androidc.utils.LauncherIcon
import com.houvven.androidc.utils.LocaleUtils
import com.houvven.twig.MainActivity
import com.houvven.twig.R
import com.houvven.twig.config.AppConfig
import com.houvven.twig.ui.LanguageSupport
import com.houvven.twig.ui.LocalAppState
import com.houvven.twig.ui.components.layout.ScrollColumn
import com.houvven.twig.ui.route.LauncherScreenTypes
import com.houvven.twig.ui.route.Routes
import com.houvven.twig.ui.route.setCurrentLauncherScreenType
import com.houvven.twig.ui.theme.ThemeMode
import com.houvven.twig.ui.utils.antiShakeNavOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLauncher() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.title_settings)) }) }
    ) {
        val launcherName = MainActivity::class.java.name + "Launcher"
        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current
        var launcherIconIsHide by remember {
            mutableStateOf(
                LauncherIcon.isHide(
                    context,
                    launcherName
                )
            )
        }
        val languageDialogShowState = remember { mutableStateOf(false) }
        val themeModeDialogShowState = remember { mutableStateOf(false) }
        LanguageDialog(languageDialogShowState)
        ThemeModeDialog(themeModeDialogShowState)
        ScrollColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            Title { stringResource(id = R.string.setting_language) }
            ItemContainer(
                icon = Icons.Outlined.Language,
                text = { stringResource(id = R.string.setting_language) },
                subText = {
                    if (LocalAppState.language.value.isBlank()) {
                        stringResource(id = R.string.follow_system)
                    } else {
                        LocaleUtils.fromString(LocalAppState.language.value).displayName
                    }
                },
                onClick = { languageDialogShowState.value = true }
            )
            Title { stringResource(id = R.string.setting_theme) }
            ItemContainer(
                icon = Icons.Outlined.Style,
                text = { stringResource(id = R.string.setting_theme_mode) },
                subText = { LocalAppState.themeMode.value },
                onClick = { themeModeDialogShowState.value = true }
            )
            SwitchItemContainer(
                icon = Icons.Outlined.ColorLens,
                text = { stringResource(id = R.string.setting_theme_monet_color) },
                subText = { stringResource(id = R.string.setting_theme_monet_color_sub) },
                checked = LocalAppState.monetColoring.value,
                onCheckedChange = {
                    LocalAppState.monetColoring.value = !LocalAppState.monetColoring.value
                    AppConfig.run {
                        mmkv {
                            encode(MONET_COLORING, LocalAppState.monetColoring.value)
                        }
                    }
                }
            )
            Title { stringResource(id = R.string.setting_app) }
            SwitchItemContainer(
                icon = Icons.Outlined.Circle,
                text = { stringResource(id = R.string.setting_hide_launcher_icon) },
                subText = { stringResource(id = R.string.setting_hide_launcher_icon_sub) },
                checked = launcherIconIsHide,
                onCheckedChange = {
                    launcherIconIsHide = if (launcherIconIsHide) {
                        LauncherIcon.show(context, launcherName)
                        false
                    } else {
                        LauncherIcon.hide(context, launcherName)
                        true
                    }
                }
            )
            SwitchItemContainer(
                icon = Icons.Outlined.WorkOutline,
                text = { stringResource(id = R.string.setting_root_free_mode) },
                subText = { stringResource(id = R.string.setting_root_free_mode_sub) },
                checked = LocalAppState.noRootWork.value,
                onCheckedChange = {
                    LocalAppState.noRootWork.value = !LocalAppState.noRootWork.value
                    LocalAppState.isGlobalPattern.value = false
                    AppConfig.run {
                        val global = sp { getBoolean(GLOBAL_PATTERN, false) }
                        spEdit {
                            putBoolean(NO_ROOT_WORK, LocalAppState.noRootWork.value)
                            if (global) putBoolean(GLOBAL_PATTERN, false)
                        }
                    }
                }
            )
            SwitchItemContainer(
                checked = LocalAppState.isGlobalPattern.value,
                onCheckedChange = {
                    LocalAppState.isGlobalPattern.value = !LocalAppState.isGlobalPattern.value
                    LocalAppState.noRootWork.value = false
                    AppConfig.run {
                        val noRoot = sp { getBoolean(NO_ROOT_WORK, false) }
                        spEdit {
                            putBoolean(GLOBAL_PATTERN, LocalAppState.isGlobalPattern.value)
                            if (noRoot) putBoolean(NO_ROOT_WORK, false)
                        }
                    }
                },
                icon = Icons.Outlined.Public,
                text = { stringResource(id = R.string.setting_global_mode) },
                subText = { stringResource(id = R.string.setting_global_mode_sub) },
                enable = LocalAppState.isHooked && !LocalAppState.noRootWork.value
            )
            Title { stringResource(id = R.string.setting_info) }
            NavItemContainer(
                icon = Icons.Outlined.Info,
                text = { stringResource(id = R.string.setting_about) },
                onClick = { antiShakeNavOption(Routes.ABOUT.name) }
            )
            NavItemContainer(
                icon = Icons.Outlined.HelpOutline,
                text = { stringResource(id = R.string.setting_help) },
                onClick = { antiShakeNavOption(Routes.HELP.name) }
            )
            NavItemContainer(
                icon = Icons.Outlined.Feedback,
                text = { stringResource(id = R.string.setting_feedback) },
                onClick = { uriHandler.openUri("https://github.com/Houvven/Twig/issues") }
            )
            NavItemContainer(
                icon = Icons.Outlined.Paid,
                text = { stringResource(id = R.string.setting_reward) },
                onClick = { antiShakeNavOption(Routes.REWARD.name) }
            )
            NavItemContainer(
                icon = Icons.Outlined.WorkspacePremium,
                text = { stringResource(id = R.string.setting_license) },
                onClick = { antiShakeNavOption(Routes.LICENSE.name) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
private fun ThemeModeDialog(displayState: MutableState<Boolean>) {
    arrayOf(
        CheckBoxGroupItem(
            icon = Icons.Outlined.BrightnessAuto,
            text = { ThemeMode.AUTO.label },
            value = ThemeMode.AUTO.label
        ),
        CheckBoxGroupItem(
            icon = Icons.Outlined.LightMode,
            text = { ThemeMode.LIGHT.label },
            value = ThemeMode.LIGHT.label
        ),
        CheckBoxGroupItem(
            icon = Icons.Outlined.DarkMode,
            text = { ThemeMode.DARK.label },
            value = ThemeMode.DARK.label
        )
    ).let { items ->
        CheckboxGroupDialog(title = "Theme Mode", displayState = displayState, items = items) {
            LocalAppState.themeMode.value = it.value
            AppConfig.run { mmkv { encode(THEME_MODE, it.value) } }
            displayState.value = false
        }
    }
}


@Composable
private fun LanguageDialog(displayState: MutableState<Boolean>) {
    val context = LocalContext.current
    val groupItems = LanguageSupport.values()
        .map { CheckBoxGroupItem(text = { it.label }, value = it.value) }
        .toMutableList()
    groupItems.add(
        0,
        CheckBoxGroupItem(text = { stringResource(id = R.string.follow_system) }, value = "")
    )
    CheckboxGroupDialog(
        title = stringResource(id = R.string.setting_language),
        displayState = displayState,
        items = groupItems.toTypedArray()
    ) {
        LocalAppState.language.value = it.value
        AppConfig.run { mmkv { encode(LANGUAGE, it.value) } }
        LocaleUtils.set(context, LocaleUtils.fromString(it.value))
        displayState.value = false
        setCurrentLauncherScreenType(LauncherScreenTypes.DEPLOYMENT)
        setCurrentLauncherScreenType(LauncherScreenTypes.SETTINGS)
    }
}
