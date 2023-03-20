package com.houvven.twig.ui.route.launcher.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.houvven.twig.ui.components.OneBtnAlterDialog

@Composable
internal fun Title(text: @Composable () -> String) {
    Text(
        text = text(),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 25.dp, top = 30.dp, bottom = 5.dp)
    )
}


@Composable
private fun BasicItemContainer(
    icon: ImageVector,
    text: @Composable () -> String,
    subText: (@Composable () -> String)? = null,
    onClick: () -> Unit = {},
    tailContent: @Composable () -> Unit = {},
    padding: PaddingValues = PaddingValues(vertical = 15.dp),
    enable: Boolean = true
) {
    Row(modifier = Modifier
        .clickable { if (enable) onClick() }
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .zIndex(1F)
                    .fillMaxWidth(0.75F),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(25.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(text = text(), style = MaterialTheme.typography.bodyLarge)
                    subText?.let {
                        Text(
                            text = it(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            if (tailContent != {}) tailContent()
        }
    }
}


@Composable
internal fun ItemContainer(
    icon: ImageVector,
    text: @Composable () -> String,
    subText: (@Composable () -> String)? = null,
    onClick: () -> Unit = {},
    enable: Boolean = true
) {
    BasicItemContainer(
        icon = icon,
        text = text,
        subText = subText,
        onClick = onClick,
        enable = enable
    )
}


@Composable
internal fun SwitchItemContainer(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    icon: ImageVector,
    text: @Composable () -> String,
    subText: (@Composable () -> String)? = null,
    enable: Boolean = true
) {
    BasicItemContainer(
        icon = icon,
        text = text,
        subText = subText,
        onClick = onCheckedChange,
        padding = PaddingValues(vertical = 8.dp),
        tailContent = {
            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange() },
                modifier = Modifier.padding(end = 15.dp),
                enabled = enable
            )
        },
        enable = enable
    )
}


@Composable
internal fun NavItemContainer(
    icon: ImageVector,
    text: @Composable () -> String,
    subText: (@Composable () -> String)? = null,
    onClick: () -> Unit = {},
) {
    BasicItemContainer(
        icon = icon,
        text = text,
        subText = subText,
        onClick = onClick,
        padding = PaddingValues(vertical = 12.dp)
    )
}


internal data class CheckBoxGroupItem<T>(
    val icon: ImageVector? = null,
    val text: @Composable () -> String,
    val value: T
)

@Composable
internal fun <T> CheckboxGroupDialog(
    title: String? = null,
    displayState: MutableState<Boolean>,
    items: Array<CheckBoxGroupItem<T>>,
    onClick: (CheckBoxGroupItem<T>) -> Unit
) {
    if (!displayState.value) return

    @Composable
    fun container(item: CheckBoxGroupItem<T>) {
        Row(modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick(item) }
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .requiredWidth(175.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item.icon?.let {
                    Icon(imageVector = it, contentDescription = null)
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Text(
                    text = item.text(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    OneBtnAlterDialog(
        onDismissRequest = { },
        title = { title?.let { Text(text = it) } },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .985F),
        tonalElevation = 0.dp,
        button = {
            TextButton(onClick = { displayState.value = false }) {
                Text(text = "Cancel")
            }
        }
    ) {
        items.forEach { container(it) }
    }
}
