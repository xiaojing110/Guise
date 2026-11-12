package com.houvven.twig.ui.route.launcher

import android.os.Environment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.houvven.twig.R
import java.io.File
import kotlin.concurrent.thread

data class LogEntry(
    val packageName: String,
    val line: String
) {
    val isError: Boolean
        get() = line.contains(" E/") || line.contains("FATAL EXCEPTION")
}

/**
 * 汇总各目标应用进程写入的 twig.log。
 *
 * Xposed 日志由目标应用进程产生，统一写到
 * `/sdcard/Android/media/<包名>/twig.log`，这里读取并展示。
 */
object LogRepository {

    private const val LOG_FILE_NAME = "twig.log"

    private const val MAX_ENTRIES = 2000

    private val _entries = mutableStateOf<List<LogEntry>>(emptyList())

    val entries: List<LogEntry> get() = _entries.value

    private val mediaDir: File
        get() = Environment.getExternalStorageDirectory().resolve("Android/media")

    fun refresh() {
        thread(name = "guise-log-reader") {
            _entries.value = readAll()
        }
    }

    fun clear() {
        thread(name = "guise-log-cleaner") {
            runCatching {
                mediaDir.listFiles()
                    ?.filter { it.isDirectory }
                    ?.forEach { dir ->
                        File(dir, LOG_FILE_NAME).delete()
                        File(dir, "$LOG_FILE_NAME.1").delete()
                    }
            }
            _entries.value = emptyList()
        }
    }

    private fun readAll(): List<LogEntry> {
        val dirs = runCatching {
            mediaDir.listFiles()
                ?.filter { it.isDirectory }
                ?.sortedByDescending { it.lastModified() }
                .orEmpty()
        }.getOrDefault(emptyList())

        val result = mutableListOf<LogEntry>()
        dirs.forEach { dir ->
            val file = File(dir, LOG_FILE_NAME)
            if (!file.exists()) return@forEach
            val lines = runCatching { file.readLines() }.getOrDefault(emptyList())
            lines.asReversed().forEach { line ->
                if (line.isNotBlank()) result.add(LogEntry(dir.name, line))
                if (result.size >= MAX_ENTRIES) return result
            }
        }
        return result
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogLauncher() {
    LaunchedEffect(Unit) { LogRepository.refresh() }

    val entries = LogRepository.entries

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.launcher_page_log),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = { LogRepository.refresh() }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(id = R.string.log_refresh)
                        )
                    }
                    IconButton(onClick = { LogRepository.clear() }) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteSweep,
                            contentDescription = stringResource(id = R.string.log_clear)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.log_empty),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 28.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize()
            ) {
                itemsIndexed(items = entries, key = { index, _ -> index }) { _, entry ->
                    Text(
                        text = entry.line,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        color = if (entry.isError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
