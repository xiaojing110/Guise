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
import com.houvven.twig.ui.route.NavBackButton
import com.houvven.twig.ui.route.deploy.DSD
import com.houvven.twig.ui.route.deploy.DeployScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditConfiguration(title: String, name: String, packageName: String) {
    val context = LocalContext.current
    val manager = remember { TwigConfig.Manager(TwigStore.APP.get(packageName)) }
    DeployScreen(manager = manager) {
        TopAppBar(
            title = {
                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Text(text = name, style = MaterialTheme.typography.titleSmall)
                }
            },
            actions = {
                DSD.ClearButton(manager = this@DeployScreen)
                DSD.SaveButton(manager = this@DeployScreen)
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                    if (showMenu) {
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.stop_app)) },
                                onClick = { stopApp(context, packageName) })
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.restart_app)) },
                                onClick = { restartApp(context, packageName) }
                            )
                        }
                    }
                }
            },
            navigationIcon = { NavBackButton() }
        )
    }
}