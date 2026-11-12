package com.houvven.twig.ui.route.screen.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.houvven.twig.R
import com.houvven.twig.TwigStore
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.ui.GlobalSnackbarHost
import com.houvven.twig.ui.components.SaveTemplateDialog
import com.houvven.twig.ui.components.TemplatePickerDialog
import com.houvven.twig.ui.route.NavBackButton
import com.houvven.twig.ui.route.deploy.DSD
import com.houvven.twig.ui.route.deploy.DeployScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditConfiguration(title: String, name: String, packageName: String) {
    val context = LocalContext.current
    val manager = remember { TwigConfig.Manager(TwigStore.APP.get(packageName)) }
    val showSaveTemplate = remember { mutableStateOf(false) }
    val showTemplatePicker = remember { mutableStateOf(false) }

    SaveTemplateDialog(
        show = showSaveTemplate,
        defaultName = listOf(manager.state.MANUFACTURER.value, manager.state.MODEL.value)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { context.getString(R.string.template_unnamed) }
    ) { templateName ->
        DSD.saveTemplate(manager, templateName)
        GlobalSnackbarHost.show(context.getString(R.string.template_saved, templateName))
    }

    TemplatePickerDialog(show = showTemplatePicker) { template ->
        DSD.applyTemplate(manager, template)
        GlobalSnackbarHost.show(context.getString(R.string.template_applied, template.name))
    }

    DeployScreen(manager = manager) {
        TopAppBar(
            title = {
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Text(text = name, style = MaterialTheme.typography.titleSmall)
                }
            },
            actions = {
                DSD.RandomButton(manager = this@DeployScreen)
                DSD.ClearButton(manager = this@DeployScreen)
                DSD.SaveButton(manager = this@DeployScreen)
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                    if (showMenu) {
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.random_action)) },
                                onClick = {
                                    showMenu = false
                                    val config = DSD.randomize(manager)
                                    GlobalSnackbarHost.show(
                                        context.getString(
                                            R.string.random_applied,
                                            config.MODEL,
                                            config.RELEASE
                                        )
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.template_save)) },
                                onClick = {
                                    showMenu = false
                                    showSaveTemplate.value = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.template_apply)) },
                                onClick = {
                                    showMenu = false
                                    showTemplatePicker.value = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.stop_app)) },
                                onClick = {
                                    showMenu = false
                                    stopApp(context, packageName)
                                })
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.restart_app)) },
                                onClick = {
                                    showMenu = false
                                    restartApp(context, packageName)
                                }
                            )
                        }
                    }
                }
            },
            navigationIcon = { NavBackButton() }
        )
    }
}
