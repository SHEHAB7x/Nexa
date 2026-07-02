package com.example.newsapp.presentation.detail.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.presentation.detail.TtsState

// TtsControls composable — goes in ArticleDetailScreen.kt or components
@Composable
fun TtsControls(
    ttsState: TtsState,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // the button changes icon based on state
        IconButton(
            onClick  = onToggle,
            // disable button while initializing
            enabled  = ttsState !is TtsState.Initializing
        ) {
            when (ttsState) {
                is TtsState.Initializing -> {
                    CircularProgressIndicator(
                        modifier  = Modifier.size(24.dp),
                        color     = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
                is TtsState.Playing -> {
                    Icon(
                        imageVector        = Icons.Default.StopCircle,
                        contentDescription = "Stop reading",
                        tint               = MaterialTheme.colorScheme.primary,
                        modifier           = Modifier.size(32.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector        = Icons.Default.PlayCircle,
                        contentDescription = "Read article",
                        tint               = MaterialTheme.colorScheme.primary,
                        modifier           = Modifier.size(32.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = when (ttsState) {
                    is TtsState.Initializing -> "Preparing..."
                    is TtsState.Playing      -> "Reading article..."
                    is TtsState.Error        -> "Could not read article"
                    else                     -> "Listen to article"
                },
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text     = "Text to speech",
                fontSize = 11.sp,
                color    = MaterialTheme.colorScheme.outline
            )
        }

        // animated speaker icon — pulses when playing
        val infiniteTransition = rememberInfiniteTransition(label = "speaker")
        val scale by infiniteTransition.animateFloat(
            initialValue  = 1f,
            targetValue   = if (ttsState is TtsState.Playing) 1.2f else 1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(600),
                repeatMode = RepeatMode.Reverse
            ),
            label = "speaker scale"
        )

        Icon(
            imageVector        = Icons.Default.VolumeUp,
            contentDescription = null,
            tint               = if (ttsState is TtsState.Playing)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline,
            modifier           = Modifier
                .size(22.dp)
                .scale(scale)
        )
    }
}