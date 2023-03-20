package com.houvven.twig.ui.components.application

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.houvven.twig.R
import com.houvven.twig.config.AppConfig
import com.houvven.twig.ui.LocalAppState
import com.houvven.twig.ui.components.NoBtnAlertDialog

enum class AppInfoSortType(val label: @Composable () -> String) {
    NAME({ stringResource(id = R.string.app_sort_name) }),
    INSTALL_TIME({ stringResource(id = R.string.app_sort_install_time) }),
    UPDATE_TIME({ stringResource(id = R.string.app_sort_update_time) })
}


private val modifier = Modifier
    .fillMaxWidth()
    .clip(RoundedCornerShape(10.dp))

@Composable
fun RadioItem(text: String, value: AppInfoSortType, state: MutableState<AppInfoSortType>) {
    Row(
        modifier = modifier.clickable { state.value = value },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = value == state.value, onClick = { state.value = value })
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun CheckboxItem(text: String, state: MutableState<Boolean>) {
    Row(
        modifier = modifier.clickable(onClick = { state.value = !state.value }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = state.value, onCheckedChange = { state.value = it })
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun AppOptionsMenu(
    display: Boolean,
    displaySystemAppsCheckbox: Boolean = true,
    displayAlsoSearchByPackageNameCheckbox: Boolean = true,
    displayAppsReverseOrderCheckbox: Boolean = true,
    onDismissRequest: () -> Unit
) {
    if (display.not()) return
    NoBtnAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        DisposableEffect(Unit) {
            onDispose {
                AppConfig.run {
                    mmkv {
                        putString(APP_SORT_TYPE, LocalAppState.Apps.sortType.value.name)
                        putBoolean(SHOW_SYSTEM_APP, LocalAppState.Apps.showSystemApps.value)
                        putBoolean(
                            ALSO_SEARCH_PACKAGE_NAME,
                            LocalAppState.Apps.alsoSearchByPackageName.value
                        )
                        putBoolean(APP_RESERVE_SORT, LocalAppState.Apps.reverseOrder.value)
                    }
                }
            }
        }
        Column {
            AppInfoSortType.values().forEach {
                RadioItem(
                    text = it.label(),
                    value = it,
                    state = LocalAppState.Apps.sortType
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4F)
            )

            if (displaySystemAppsCheckbox) CheckboxItem(
                text = stringResource(id = R.string.show_system_apps),
                state = LocalAppState.Apps.showSystemApps
            )
            if (displayAlsoSearchByPackageNameCheckbox) CheckboxItem(
                text = stringResource(id = R.string.also_search_by_package_name),
                state = LocalAppState.Apps.alsoSearchByPackageName
            )
            if (displayAppsReverseOrderCheckbox) CheckboxItem(
                text = stringResource(id = R.string.apps_reverse_order),
                state = LocalAppState.Apps.reverseOrder
            )
        }
    }
}