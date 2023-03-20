package com.houvven.twig.ui.components.application

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.houvven.androidc.application.AppInfo
import com.houvven.androidc.ktx.ifTrue
import com.houvven.twig.ui.LocalAppState

@Immutable
data class AppInfoWithCompose(
    val info: AppInfo
) {
    val icon = info.icon?.toBitmap()?.asImageBitmap()
}

fun List<AppInfo>.toAppInfoWithCompose() = map { AppInfoWithCompose(it) }

@Composable
fun AppInfoCardContent(
    app: AppInfoWithCompose,
    displayVersion: Boolean = true,
    displayIcon: Boolean = true,
) {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        displayIcon ifTrue {
            app.icon?.let {
                Image(
                    bitmap = app.icon,
                    contentDescription = app.info.label,
                    modifier = Modifier.size(40.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(text = app.info.label, style = MaterialTheme.typography.titleSmall)
            Text(text = app.info.packageName, style = MaterialTheme.typography.labelLarge)
            displayVersion ifTrue {
                val version = "${app.info.versionName}(${app.info.versionCode})"
                Text(
                    text = version,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}


@Composable
fun AppListView(
    apps: List<AppInfoWithCompose>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemContent: @Composable (AppInfoWithCompose) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 6.dp),
        state = state
    ) {
        items(
            items = apps,
            key = { it.info.packageName }
        ) {
            itemContent(it)
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    LocalAppState.Apps.refreshIfNull(context)
}