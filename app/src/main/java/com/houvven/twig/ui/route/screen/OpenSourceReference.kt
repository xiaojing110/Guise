package com.houvven.twig.ui.route.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.houvven.twig.R
import com.houvven.twig.ui.route.NavBackButton

@Immutable
enum class OpenSourceReference(val label: String, val url: String) {
    Accompanist("Accompanist", "https://google.github.io/accompanist/"),
    Compose("Compose", "https://developer.android.com/jetpack/compose"),
    KotlinSerialization("Kotlin Serialization", "https://github.com/Kotlin/kotlinx.serialization"),
    Ktor("Ktor", "https://ktor.io/"),
    MMKV("MMKV", "https://github.com/Tencent/MMKV"),
    Room("Room", "https://developer.android.com/training/data-storage/room"),
    XposedBridge("XposedBridge", "https://github.com/rovo89/XposedBridge")
    ;
}

@Composable
private fun OpenSourceReferenceCard(name: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(url) }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = url,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.SansSerif
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceReference() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.setting_license)) },
                navigationIcon = { NavBackButton() }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            items(OpenSourceReference.values()) { reference ->
                OpenSourceReferenceCard(name = reference.label, url = reference.url)
            }
        }
    }
}