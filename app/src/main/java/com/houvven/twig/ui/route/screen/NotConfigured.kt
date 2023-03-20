package com.houvven.twig.ui.route.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.houvven.twig.R
import com.houvven.twig.TwigStore
import com.houvven.twig.ui.LocalAppState
import com.houvven.twig.ui.components.application.AppInfoCardContent
import com.houvven.twig.ui.components.application.AppInfoWithCompose
import com.houvven.twig.ui.components.application.AppListView
import com.houvven.twig.ui.components.application.AppOptionsMenu
import com.houvven.twig.ui.route.NavBackButton
import com.houvven.twig.ui.route.Routes
import com.houvven.twig.ui.utils.antiShakeNavOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotConfiguredScreen() {
    var isSearchOpen by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var isOpenMenu by remember { mutableStateOf(false) }

    fun cancelSearch() {
        isSearchOpen = false
        query = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchOpen) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            placeholder = { Text(stringResource(com.airbnb.lottie.R.string.abc_search_hint)) },
                            leadingIcon = { Icon(Icons.Default.Search, null) },
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Text(text = stringResource(id = R.string.title_config_not_configured))
                    }
                },
                actions = {
                    if (!isSearchOpen) {
                        IconButton(onClick = { isSearchOpen = true }) {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                        }
                    } else {
                        IconButton(onClick = { cancelSearch() }) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                        }
                    }
                    IconButton(onClick = { isOpenMenu = true }) {
                        Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                        AppOptionsMenu(display = isOpenMenu) { isOpenMenu = false }
                    }
                },
                navigationIcon = { if (!isSearchOpen) NavBackButton() }
            )
        },
    ) { paddingValues ->
        val predicate: (AppInfoWithCompose) -> Boolean =
            { TwigStore.APP.all.value.contains(it.info.packageName) }

        var apps = LocalAppState.Apps.current.value.filterNot(predicate)
        if (isSearchOpen && query.isNotBlank()) {
            apps = if (LocalAppState.Apps.alsoSearchByPackageName.value) {
                apps.filter { it.info.label.contains(query, ignoreCase = true) }
            } else {
                apps.filter {
                    it.info.label.contains(query, ignoreCase = true)
                            || it.info.packageName.contains(query, ignoreCase = true)
                }
            }
        }
        LocalAppState.Apps.current.value
        AppListView(
            apps = apps,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            val title = stringResource(id = R.string.title_config_add)
            Card(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .clickable {
                        antiShakeNavOption(
                            route = "${Routes.CONFIGURATION_EDIT.name}/$title/${it.info.label}/${it.info.packageName}",
                        )
                    },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2F)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    AppInfoCardContent(app = it)
                }
            }
        }
    }
}