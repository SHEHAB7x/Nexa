package com.example.newsapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoInternetBanner(isVisible: Boolean) {

    AnimatedVisibility(
        visible = !isVisible,
        enter   = slideInVertically(initialOffsetY = { -it }),
        exit    = slideOutVertically(targetOffsetY = { -it })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB00020))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector        = Icons.Default.Clear,  //Wifi OFF
                contentDescription = null,
                tint               = Color.White,
                modifier           = Modifier.size(18.dp)
            )
            Text(
                text       = "No internet connection",
                color      = Color.White,
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}