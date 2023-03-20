package com.houvven.twig.ui.components.application

import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun AppListPlaceholder(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.surfaceVariant
    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15F)

    val placeholder = Modifier.placeholder(
        visible = true,
        color = color,
        shape = MaterialTheme.shapes.small,
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = highlightColor,
            progressForMaxAlpha = 0.3F,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, delayMillis = 100, easing = EaseOutSine),
                repeatMode = RepeatMode.Restart
            )
        )
    )

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(10) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .size(45.dp)
                        .then(placeholder)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Spacer(
                        modifier = Modifier
                            .height(22.dp)
                            .width(150.dp)
                            .then(placeholder)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Spacer(
                        modifier = Modifier
                            .height(18.dp)
                            .width(225.dp)
                            .then(placeholder)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Spacer(
                        modifier = Modifier
                            .height(14.dp)
                            .width(100.dp)
                            .then(placeholder)
                    )
                }
            }
        }
    }
}