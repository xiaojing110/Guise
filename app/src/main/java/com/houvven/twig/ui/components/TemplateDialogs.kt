package com.houvven.twig.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.houvven.twig.R
import com.houvven.twig.config.ConfigTemplate
import com.houvven.twig.config.TemplateStore

/**
 * 保存当前配置为模板。
 */
@Composable
fun SaveTemplateDialog(
    show: MutableState<Boolean>,
    defaultName: String,
    onSave: (String) -> Unit
) {
    if (!show.value) return
    var name by remember { mutableStateOf(defaultName) }

    AlertDialog(
        onDismissRequest = { show.value = false },
        title = { Text(text = stringResource(id = R.string.template_save)) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.template_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(name.trim())
                        show.value = false
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.template_save_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { show.value = false }) {
                Text(text = stringResource(id = R.string.template_cancel))
            }
        }
    )
}

/**
 * 从模板列表中选择一个模板应用到当前配置。
 */
@Composable
fun TemplatePickerDialog(
    show: MutableState<Boolean>,
    onPick: (ConfigTemplate) -> Unit
) {
    if (!show.value) return

    AlertDialog(
        onDismissRequest = { show.value = false },
        title = { Text(text = stringResource(id = R.string.template_apply)) },
        text = {
            val templates = TemplateStore.templates
            Column {
                if (templates.isEmpty()) {
                    Text(text = stringResource(id = R.string.template_empty))
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 380.dp)) {
                        items(items = templates, key = { it.id }) { template ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onPick(template)
                                        show.value = false
                                    }
                                    .padding(vertical = 9.dp)
                            ) {
                                Text(
                                    text = template.name,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = template.summary,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = { show.value = false }) {
                Text(text = stringResource(id = R.string.template_cancel))
            }
        }
    )
}
