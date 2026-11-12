package com.houvven.twig.ui.route.launcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.houvven.twig.R
import com.houvven.twig.TwigStore
import com.houvven.twig.config.ConfigTemplate
import com.houvven.twig.config.RandomConfigGenerator
import com.houvven.twig.config.TemplateStore
import com.houvven.twig.ui.GlobalSnackbarHost
import com.houvven.twig.ui.LocalAppState
import com.houvven.twig.ui.components.application.AppInfoWithCompose
import com.houvven.twig.ui.route.deploy.DSD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateLauncher() {
    val context = LocalContext.current
    val templates = TemplateStore.templates
    var pendingApply by remember { mutableStateOf<ConfigTemplate?>(null) }
    var pendingDelete by remember { mutableStateOf<ConfigTemplate?>(null) }

    LaunchedEffect(Unit) {
        LocalAppState.Apps.refreshIfNull(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.launcher_page_template),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val (config, modelName) = RandomConfigGenerator.generateWithName(context)
                    val name = context.getString(
                        R.string.template_random_name,
                        config.MANUFACTURER,
                        modelName
                    )
                    val template = TemplateStore.save(name, config)
                    GlobalSnackbarHost.show(
                        context.getString(R.string.template_random_saved, template.name)
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Casino,
                    contentDescription = stringResource(id = R.string.random_action)
                )
            }
        }
    ) { paddingValues ->
        if (templates.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.template_empty),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 28.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = templates, key = { it.id }) { template ->
                    TemplateCard(
                        template = template,
                        onApply = { pendingApply = template },
                        onDelete = { pendingDelete = template }
                    )
                }
            }
        }
    }

    pendingApply?.let { template ->
        AppPickerDialog(
            template = template,
            onDismiss = { pendingApply = null },
            onPick = { app ->
                DSD.applyTemplateToPackage(template, app.info.packageName)
                pendingApply = null
                GlobalSnackbarHost.show(
                    context.getString(
                        R.string.template_applied_to,
                        template.name,
                        app.info.label
                    )
                )
            }
        )
    }

    pendingDelete?.let { template ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(text = stringResource(id = R.string.template_delete)) },
            text = { Text(text = stringResource(id = R.string.template_delete_confirm, template.name)) },
            confirmButton = {
                TextButton(onClick = {
                    TemplateStore.delete(template.id)
                    pendingDelete = null
                    GlobalSnackbarHost.show(
                        context.getString(R.string.template_deleted, template.name)
                    )
                }) {
                    Text(text = stringResource(id = R.string.template_delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(text = stringResource(id = R.string.template_cancel))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateCard(
    template: ConfigTemplate,
    onApply: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32F)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = template.name,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                if (template.builtIn) {
                    ElevatedSuggestionChip(
                        onClick = {},
                        label = { Text(text = stringResource(id = R.string.template_builtin)) }
                    )
                } else {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(id = R.string.template_delete),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            Text(
                text = template.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
            if (template.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = template.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onApply) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = stringResource(id = R.string.template_apply))
                }
            }
        }
    }
}

@Composable
private fun AppPickerDialog(
    template: ConfigTemplate,
    onDismiss: () -> Unit,
    onPick: (AppInfoWithCompose) -> Unit
) {
    val apps = LocalAppState.Apps.current.value
        .sortedWith(
            compareByDescending<AppInfoWithCompose> {
                TwigStore.APP.all.value.contains(it.info.packageName)
            }.thenBy { it.info.label }
        )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.template_apply_to, template.name)) },
        text = {
            Column {
                if (LocalAppState.Apps.appsIsLoading.value) {
                    Text(text = stringResource(id = R.string.template_apps_loading))
                } else if (apps.isEmpty()) {
                    Text(text = stringResource(id = R.string.template_apps_empty))
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 420.dp)) {
                        items(items = apps, key = { it.info.packageName }) { app ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPick(app) }
                                    .padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = app.info.label,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = app.info.packageName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.template_cancel))
            }
        }
    )
}
