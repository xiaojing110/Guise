package com.houvven.guise.ui.components.application

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.houvven.androidc.application.DeviceAppProvider
import com.houvven.guise.ui.AppState
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


@Composable
fun refreshApps() {
    LocalContext.current.let {
        thread {
            runBlocking {
                DeviceAppProvider.getInstalledApp(
                    context = it,
                    includeSystemApps = true,
                    needIcon = true
                )
            }.let {
                AppState.apps.value = it.toListWithCompose()
            }
        }
    }
}


@Composable
fun refreshAppsIfNull() {
    if (AppState.apps.value.isEmpty()) {
        refreshApps()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppInfoListView(itemOnclick: (AppInfoWithCompose) -> Unit) {

    LazyColumn(contentPadding = PaddingValues(horizontal = 6.dp)) {
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

    refreshAppsIfNull()

}