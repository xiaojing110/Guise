@file:Suppress("unused")

package com.houvven.guise.ui.components.application

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.houvven.guise.ui.AppState


@Composable
fun RefreshApps() {
    LocalContext.current.let { AppState.refreshApps(it) }
}

@Composable
fun RefreshAppsIfNull() {
    LocalContext.current.let { AppState.refreshAppsIfNull(it) }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppInfoListView(
    itemOnclick: (AppInfoWithCompose) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 6.dp),
        modifier = modifier,
        state = state
    ) {
        items(
            items = AppState.apps.value,
            key = { it.info.packageName },
            itemContent = { app ->
                AppInfoCard(
                    app = app,
                    onClick = itemOnclick,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier.animateItemPlacement()
                )
            }
        )
    }

    RefreshAppsIfNull()
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppInfoListView(
    apps: List<AppInfoWithCompose>,
    itemOnclick: (AppInfoWithCompose) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 6.dp),
        modifier = modifier,
        state = state
    ) {
        items(
            items = apps,
            key = { it.info.packageName },
            itemContent = { app ->
                AppInfoCard(
                    app = app,
                    onClick = itemOnclick,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier.animateItemPlacement()
                )
            }
        )
    }

    RefreshAppsIfNull()
}