package com.houvven.twig.ui.route.deploy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Circle
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.houvven.twig.preset.adapter.PresetAdapter
import com.houvven.twig.ui.LocalAppState
import kotlin.concurrent.thread


@Composable
private fun Button(icon: ImageVector, clickable: () -> Unit) {
    IconButton(onClick = clickable, modifier = Modifier.requiredSize(24.dp)) {
        Icon(
            icon,
            contentDescription = null,
            Modifier.padding(2.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.95F)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicInput(
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    state: MutableState<String>,
    label: @Composable () -> String,
    placeholder: (() -> String)? = null,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f),
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
    ),
    trailingIcon: @Composable () -> Unit = {},
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        label = { Text(text = label()) },
        placeholder = { placeholder?.let { Text(text = it()) } },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = { if (LocalAppState.isHooked) trailingIcon() },
        enabled = LocalAppState.isHooked,
        colors = colors,
        shape = MaterialTheme.shapes.medium,
        readOnly = readOnly
    )
}


@Composable
internal fun Input(
    state: MutableState<String>,
    label: @Composable () -> String,
    placeholder: (() -> String)? = null
) {
    BasicInput(
        state = state,
        label = label,
        placeholder = placeholder,
        trailingIcon = {
            if (state.value.isNotBlank()) {
                Button(Icons.TwoTone.Delete) { state.value = "" }
            }
        }
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun OperateInputBox(
    state: MutableState<String>,
    label: @Composable () -> String,
    placeholder: (() -> String)? = null,
    showOperateIcon: Boolean = true,
    clickable: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val onClick = {
        focusManager.clearFocus()
        thread { keyboardController?.hide() }
        clickable()
    }

    BasicInput(state = state, label = label, placeholder = placeholder) {
        Row {
            if (showOperateIcon) Button(Icons.TwoTone.Circle, onClick)
            if (state.value.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(Icons.TwoTone.Delete) { state.value = "" }
                Spacer(modifier = Modifier.width(13.dp))
            }
        }
    }
}


@Composable
internal fun RandomInput(
    state: MutableState<String>,
    label: @Composable () -> String,
    placeholder: (() -> String)? = null,
    randomGenerator: () -> String,
) {
    OperateInputBox(
        state = state,
        label = label,
        placeholder = placeholder,
        clickable = { state.value = randomGenerator() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropMenuInput(
    state: MutableState<String>,
    label: @Composable () -> String,
    placeholder: (() -> String)? = null,
    preset: List<PresetAdapter>,
    clickable: (String) -> Unit = { state.value = it }
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.clip(MaterialTheme.shapes.medium)
    ) {
        BasicInput(
            state = state,
            label = label,
            placeholder = placeholder,
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .requiredWidth(150.dp)
        ) {
            preset.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.label) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = {
                        clickable(item.value)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
internal fun SwitchInput(state: MutableState<Boolean>, label: @Composable () -> String) {
    OutlinedCard(
        modifier = Modifier
            .padding(top = 7.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { state.value = !state.value },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = .8f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
        ) {
            Text(
                text = label(),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = state.value,
                onCheckedChange = { state.value = it },
                modifier = Modifier.padding(end = 8.dp),
                enabled = LocalAppState.isHooked
            )
        }
    }
}