package com.houvven.twig.ui.route.deploy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.SaveAs
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.houvven.twig.config.TwigConfig
import com.houvven.twig.ui.GlobalSnackbarHost

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
                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}