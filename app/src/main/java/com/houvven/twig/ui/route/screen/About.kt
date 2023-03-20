package com.houvven.twig.ui.route.screen

import android.icu.text.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.houvven.twig.BuildConfig
import com.houvven.twig.R
import com.houvven.twig.ui.components.layout.ScrollColumn
import com.houvven.twig.ui.route.NavBackButton


@Composable
private fun SmallTitle(label: @Composable () -> String) {
    Text(
        text = label(),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(0.6F),
        modifier = Modifier.padding(top = 16.dp, bottom = 1.dp)
    )
}

@Composable
private fun UrlContent(
    fontFamily: FontFamily = FontFamily.SansSerif,
    url: @Composable () -> String
) {
    val uriHandler = LocalUriHandler.current
    val target = url()
    Text(
        text = target,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(0.9F),
        fontFamily = fontFamily,
        modifier = Modifier.clickable { uriHandler.openUri(target) }
    )
}

@Composable
private fun TextContent(
    fontFamily: FontFamily = FontFamily.SansSerif,
    onClick: () -> Unit = {},
    content: @Composable () -> String
) {
    Text(
        text = content(),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(0.9F),
        fontFamily = fontFamily,
        modifier = Modifier.clickable { onClick() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.setting_about)) }, navigationIcon = {
                NavBackButton()
            })
        }
    ) {
        ScrollColumn(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.padding(start = 30.dp)) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8F),
                    letterSpacing = 1.3.sp
                )
                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 3.dp,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .width(50.dp)
                )
                SmallTitle { stringResource(id = R.string.about_author) }
                TextContent { "Houvven" }
                SmallTitle { stringResource(id = R.string.about_version) }
                TextContent { "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})" }
                SmallTitle { stringResource(id = R.string.build_time) }
                val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                val date = dateFormat.format(BuildConfig.BUILD_TIME)
                TextContent { date }
                SmallTitle { stringResource(id = R.string.about_open_source) }
                UrlContent { "https://github.com/Houvven/Twig" }
                Spacer(modifier = Modifier.padding(top = 20.dp))
                TextContent(fontFamily = FontFamily.Cursive) {
                    "Send a warm message (what you want and need) to your Android device, like a twig it has vitality."
                }
            }
        }
    }

}