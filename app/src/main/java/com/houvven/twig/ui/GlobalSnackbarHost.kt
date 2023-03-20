package com.houvven.twig.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("unused")
@OptIn(DelicateCoroutinesApi::class)
object GlobalSnackbarHost {

    internal val state by derivedStateOf { SnackbarHostState() }

    internal val onError by derivedStateOf { mutableStateOf(false) }

    @JvmStatic
    fun show(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ) {
        if (onError.value) onError.value = false
        GlobalScope.launch { state.showSnackbar(message, actionLabel, withDismissAction, duration) }
    }

    @JvmStatic
    fun showError(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ) {
        onError.value = true
        GlobalScope.launch { state.showSnackbar(message, actionLabel, withDismissAction, duration) }
    }

    @JvmStatic
    fun showAndDismissPrevious(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ) {
        if (state.currentSnackbarData != null) {
            Thread.sleep(100)
            state.currentSnackbarData?.dismiss()
        }
        show(message, actionLabel, withDismissAction, duration)
    }

    @JvmStatic
    fun showErrorAndDismissPrevious(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ) {
        if (state.currentSnackbarData != null) {
            Thread.sleep(100)
            state.currentSnackbarData?.dismiss()
        }
        showError(message, actionLabel, withDismissAction, duration)
    }

    @JvmStatic
    fun showSuccess() {
        showAndDismissPrevious(
            message = "Success",
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
    }

}