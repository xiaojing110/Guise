package com.houvven.twig.ui.route.deploy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.SaveAs
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.houvven.twig.ContextAmbient
import com.houvven.twig.R
import com.houvven.twig.TwigStore
import com.houvven.twig.config.ConfigTemplate
import com.houvven.twig.config.RandomConfigGenerator
import com.houvven.twig.config.TemplateStore
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.ui.GlobalSnackbarHost
import com.houvven.twig.ui.components.SaveTemplateDialog

object DSD {

    @Composable
    fun ClearButton(manager: TwigConfig.Manager) {
        IconButton(onClick = {
            manager.clear()
            GlobalSnackbarHost.showSuccess()
        }) {
            Icon(
                imageVector = Icons.Outlined.DeleteSweep,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }
    }

    @Composable
    fun SaveButton(manager: TwigConfig.Manager) {
        IconButton(onClick = {
            manager.save()
            GlobalSnackbarHost.showSuccess()
        }) {
            Icon(
                imageVector = Icons.Outlined.SaveAs,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }
    }

    /**
     * 一键随机：生成完整配置、立即回填并保存，避免随机结果丢失。
     */
    fun randomize(manager: TwigConfig.Manager): TwigConfig.Config {
        val config = RandomConfigGenerator.generate(ContextAmbient.current)
        manager.apply(config)
        LocalConfigState.currentStateCache.value = manager.state
        manager.save()
        return config
    }

    /**
     * 把模板写入当前编辑中的应用，并立即保存。
     */
    fun applyTemplate(manager: TwigConfig.Manager, template: ConfigTemplate) {
        manager.apply(template.config)
        LocalConfigState.currentStateCache.value = manager.state
        manager.save()
    }

    fun saveTemplate(manager: TwigConfig.Manager, name: String): ConfigTemplate =
        TemplateStore.save(name, manager.snapshot())

    @Composable
    fun RandomButton(manager: TwigConfig.Manager) {
        val context = LocalContext.current
        IconButton(onClick = {
            val config = randomize(manager)
            GlobalSnackbarHost.show(
                context.getString(R.string.random_applied, config.MODEL, config.RELEASE)
            )
        }) {
            Icon(
                imageVector = Icons.Outlined.Casino,
                contentDescription = context.getString(R.string.random_action),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }
    }

    @Composable
    fun SaveTemplateButton(manager: TwigConfig.Manager) {
        val context = LocalContext.current
        val show = remember { mutableStateOf(false) }
        val defaultName = listOf(
            manager.state.MANUFACTURER.value,
            manager.state.MODEL.value
        ).filter { it.isNotBlank() }.joinToString(" ")

        IconButton(onClick = { show.value = true }) {
            Icon(
                imageVector = Icons.Outlined.SaveAs,
                contentDescription = context.getString(R.string.template_save),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }

        SaveTemplateDialog(
            show = show,
            defaultName = defaultName.ifBlank { context.getString(R.string.template_unnamed) }
        ) { name ->
            saveTemplate(manager, name)
            GlobalSnackbarHost.show(context.getString(R.string.template_saved, name))
        }
    }

    /**
     * 模板页 / 编辑页共用：把模板写入指定应用的持久化配置。
     */
    fun applyTemplateToPackage(template: ConfigTemplate, packageName: String) {
        TwigStore.APP.save(template.config.copy(packageName = packageName))
    }
}
