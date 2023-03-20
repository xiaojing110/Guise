package com.houvven.twig.ui.route

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Poll
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.houvven.twig.R
import com.houvven.twig.ui.route.deploy.DeployScreen
import com.houvven.twig.ui.route.launcher.ConfiguredLauncher
import com.houvven.twig.ui.route.launcher.LogLauncher
import com.houvven.twig.ui.route.launcher.settings.SettingsLauncher
import com.houvven.twig.ui.route.launcher.TemplateLauncher

@Immutable
enum class LauncherScreenTypes(
    val label: @Composable () -> String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DEPLOYMENT(
        label = { stringResource(id = R.string.launcher_page_configuration) },
        selectedIcon = Icons.Filled.Science,
        unselectedIcon = Icons.Outlined.Science
    ),
    TEMPLATE(
        label = { stringResource(id = R.string.launcher_page_template) },
        selectedIcon = Icons.Filled.Poll,
        unselectedIcon = Icons.Outlined.Poll
    ),
    LOG(
        label = { stringResource(id = R.string.launcher_page_log) },
        selectedIcon = Icons.Filled.Assignment,
        unselectedIcon = Icons.Outlined.Assignment
    ),
    SETTINGS(
        label = { stringResource(id = R.string.launcher_page_settings) },
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

private val currentPage by derivedStateOf { mutableStateOf(LauncherScreenTypes.DEPLOYMENT.name) }

fun setCurrentLauncherScreenType(type: LauncherScreenTypes) {
    currentPage.value = type.name
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LauncherScreen() {

    val systemUiController = rememberSystemUiController()
    val bottomNavBackground = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.05F)

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = bottomNavBackground
        )
    }


    @Composable
    fun RowScope.NavItem(
        type: String,
        selectedIcon: ImageVector,
        unselectedIcon: ImageVector,
        label: String,
    ) {
        val selectedColor = MaterialTheme.colorScheme.primary
        val unselectedColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6F)
        val selected = currentPage.value == type

        val (icon, color) =
            (if (selected) arrayOf(selectedIcon, selectedColor)
            else arrayOf(unselectedIcon, unselectedColor))

        val scale = if (selected) 1.1F else 1F
        NavigationBarItem(
            selected = selected,
            onClick = { currentPage.value = type },
            icon = {
                Icon(
                    imageVector = icon as ImageVector,
                    contentDescription = null,
                    tint = color as Color,
                    modifier = Modifier.scale(scale)
                )
            },
            label = { Text(text = label, modifier = Modifier.scale(scale)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
    }

    Scaffold(
        bottomBar = {
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            NavigationBar(
                containerColor = bottomNavBackground,
                tonalElevation = 0.dp
            ) {
                LauncherScreenTypes.values().forEach { screenType ->
                    NavItem(
                        type = screenType.name,
                        selectedIcon = screenType.selectedIcon,
                        unselectedIcon = screenType.unselectedIcon,
                        label = screenType.label()
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            AnimatedContent(
                targetState = currentPage, label = currentPage.value
            ) { screenTypes ->
                when (screenTypes.value) {
                    LauncherScreenTypes.DEPLOYMENT.name -> ConfiguredLauncher()
                    LauncherScreenTypes.TEMPLATE.name -> TemplateLauncher()
                    LauncherScreenTypes.LOG.name -> LogLauncher()
                    LauncherScreenTypes.SETTINGS.name -> SettingsLauncher()
                }
            }
        }
    }
}