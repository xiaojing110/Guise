@file:Suppress("unused")


package com.houvven.guise.ui.components.application

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.houvven.androidc.application.AppInfo
import com.houvven.androidc.ktx.ifTrue

data class AppInfoWithCompose(val info: AppInfo) {
    val icon = info.icon?.toBitmap()?.asImageBitmap()
}

fun AppInfo.toAppInfoWithCompose() = AppInfoWithCompose(this)

fun List<AppInfo>.toListWithCompose() = map { AppInfoWithCompose(it) }


@Immutable
class AppInfoCardTextStyle internal constructor(
    val labelStyle: TextStyle,
    val packageNameStyle: TextStyle,
    val versionStyle: TextStyle
)

@Immutable
object AppInfoCardDefaults {

    internal val defIconSize = 40.dp

    @Composable
    fun textStyle(
        labelStyle: TextStyle = MaterialTheme.typography.titleSmall,
        packageNameStyle: TextStyle = MaterialTheme.typography.bodyMedium,
        versionStyle: TextStyle = MaterialTheme.typography.labelMedium
    ) = AppInfoCardTextStyle(labelStyle, packageNameStyle, versionStyle)

}


@Composable
private fun AppInfoCardContent(
    app: AppInfoWithCompose,
    displayVersion: Boolean,
    displayIcon: Boolean,
    iconSize: Dp,
    textStyle: AppInfoCardTextStyle
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
                    modifier = Modifier.size(iconSize),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(text = app.info.label, style = textStyle.labelStyle)
            Text(text = app.info.packageName, style = textStyle.packageNameStyle)
            displayVersion ifTrue {
                val version = "${app.info.versionName}(${app.info.versionCode})"
                Text(text = version, style = textStyle.versionStyle)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoCard(
    app: AppInfoWithCompose,
    onClick: (AppInfoWithCompose) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = AppInfoCardDefaults.defIconSize,
    displayVersion: Boolean = true,
    displayIcon: Boolean = true,
    textStyle: AppInfoCardTextStyle = AppInfoCardDefaults.textStyle(),
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Card(
        onClick = { onClick(app) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource
    ) {
        AppInfoCardContent(
            app = app,
            displayVersion = displayVersion,
            displayIcon = displayIcon,
            iconSize = iconSize,
            textStyle = textStyle
        )
    }
}


@Composable
fun ElevatedAppInfoCard(
    app: AppInfoWithCompose,
    onClick: (AppInfoWithCompose) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = AppInfoCardDefaults.defIconSize,
    displayVersion: Boolean = true,
    displayIcon: Boolean = true,
    textStyle: AppInfoCardTextStyle = AppInfoCardDefaults.textStyle(),
    enabled: Boolean = true,
    shape: Shape = CardDefaults.elevatedShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AppInfoCard(
        app = app,
        onClick = onClick,
        modifier = modifier,
        iconSize = iconSize,
        displayVersion = displayVersion,
        displayIcon = displayIcon,
        textStyle = textStyle,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        interactionSource = interactionSource
    )
}


@Composable
fun OutlinedAppInfoCard(
    app: AppInfoWithCompose,
    onClick: (AppInfoWithCompose) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = AppInfoCardDefaults.defIconSize,
    displayVersion: Boolean = true,
    displayIcon: Boolean = true,
    textStyle: AppInfoCardTextStyle = AppInfoCardDefaults.textStyle(),
    enabled: Boolean = true,
    shape: Shape = CardDefaults.outlinedShape,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    border: BorderStroke = CardDefaults.outlinedCardBorder(enabled),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AppInfoCard(
        app = app,
        onClick = onClick,
        modifier = modifier,
        iconSize = iconSize,
        displayVersion = displayVersion,
        displayIcon = displayIcon,
        textStyle = textStyle,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource
    )
}

