package com.houvven.twig.ui.route.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.houvven.twig.R
import com.houvven.twig.ui.components.layout.ScrollColumn
import com.houvven.twig.ui.route.NavBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen() {
    val context = LocalContext.current
    val md = context.assets.open("md/Help.md").use { input ->
        input.bufferedReader().use { reader ->
            reader.readText()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.setting_help)) },
                navigationIcon = { NavBackButton() }
            )
        }
    ) {
        ScrollColumn(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            RichText(
                modifier = Modifier.padding(20.dp),
            ) {
                Markdown(md)
            }
        }
    }


}