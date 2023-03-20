package com.houvven.twig

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.houvven.androidc.utils.LocaleUtils
import com.houvven.twig.ui.GlobalSnackbarHost
import com.houvven.twig.ui.LocalAppState
import com.houvven.twig.ui.components.UserAgreementDialog
import com.houvven.twig.ui.route.NavigationRoute
import com.houvven.twig.ui.theme.TwigTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LocalAppState.language.value.isNotBlank()) {
            LocaleUtils.set(this, LocaleUtils.fromString(LocalAppState.language.value))
        }

        setContent {
            App()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            startActivity(Intent().apply {
                action = "android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION"
                data = Uri.parse("package:${this@MainActivity.packageName}")
            })
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            // 申请外部存储读写权限
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    TwigTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = GlobalSnackbarHost.state,
                    modifier = Modifier.systemBarsPadding(),
                ) { data ->
                    val containerStateColor =
                        if (GlobalSnackbarHost.onError.value) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    Snackbar(
                        modifier = Modifier.systemBarsPadding(),
                        snackbarData = data,
                        containerColor = containerStateColor,
                        shape = MaterialTheme.shapes.medium,
                    )
                }
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (LocalAppState.isAgreeUserAgreement.value) NavigationRoute()
                else UserAgreementDialog(LocalAppState.isAgreeUserAgreement)
            }
        }
    }
}