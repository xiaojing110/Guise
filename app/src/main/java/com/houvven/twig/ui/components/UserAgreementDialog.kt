package com.houvven.twig.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.houvven.androidc.ktx.showToast
import com.houvven.twig.config.AppConfig
import com.houvven.twig.ui.components.layout.ScrollColumn
import kotlin.concurrent.thread

@Composable
fun UserAgreementDialog(agreed: MutableState<Boolean>) {
    if (agreed.value) return
    val context = LocalContext.current
    val md = context.assets.open("md/Agreement.md").use { input ->
        input.bufferedReader().use { reader ->
            reader.readText()
        }
    }
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        dismissButton = {},
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95F),
        text = {
            ScrollColumn {
                RichText(
                    modifier = Modifier
                        .zIndex(2F)
                        .fillMaxWidth()
                ) {
                    Markdown(md)
                    SideEffect {
                        context.showToast("请仔细阅读本软件的使用协议.")
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(.5F),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        AppConfig.run { mmkv { encode(AGREE_USER_AGREEMENT, true) } }
                        agreed.value = true
                    }) {
                        Text("Agree")
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    OutlinedButton(
                        onClick = {
                            context.showToast("由于你拒绝了本软件的使用协议，软件将在3s后退出。")
                            thread {
                                Thread.sleep(3000)
                                throw RuntimeException("The user has rejected the software usage agreement, so the application process is terminated. ")
                            }
                        }
                    ) {
                        Text("Deny")
                    }
                }
            }
        }
    )
}