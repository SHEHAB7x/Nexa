package com.example.newsapp.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.R
import com.example.newsapp.presentation.components.AppIcons
import com.example.newsapp.presentation.theme.NewsAppTheme
import com.google.firebase.BuildConfig

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    val openUrl: (String) -> Unit = { url ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SA",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Shehab Abdelhares",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Android Developer",
                    fontSize = 14.sp,
                    color    = Color.White.copy(alpha = 0.85f)
                )

                Text(
                    text     = "Qena, Egypt 🇪🇬",
                    fontSize = 13.sp,
                    color    = Color.White.copy(alpha = 0.75f)
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        ProfileSectionHeader(title = "Connect")
        ProfileLinkItem(
            icon       = AppIcons.GitHub,
            label      = "GitHub",
            value      = "SHEHAB7x",
            tint       = Color(0xFF333333),
            onClick    = { openUrl("https://github.com/SHEHAB7x") }
        )
        ProfileLinkItem(
            icon       = AppIcons.LinkedIn,
            label      = "LinkedIn",
            value      = "shehab0x",
            tint       = Color(0xFF0077B5),
            onClick    = { openUrl("https://www.linkedin.com/in/shehab0x") }
        )

        ProfileLinkItem(
            icon       = AppIcons.Email,
            label      = "Email",
            value      = "sabdalhares@gmail.com",
            tint       = MaterialTheme.colorScheme.primary,
            onClick    = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data    = Uri.parse("mailto:sabdalhares@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Hello from NewsApp")
                }
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileSectionHeader(title = "App")
        ProfileInfoItem(
            icon  = Icons.Default.Info,
            label = "App Name",
            value = "Nexa"
        )

        ProfileInfoItem(
            icon  = Icons.Default.Info,
            label = "Version",
            value = BuildConfig.VERSION_NAME
        )

        ProfileInfoItem(
            icon  = Icons.Default.Info,
            label = "Powered by",
            value = "NewsAPI.org"
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProfileSectionHeader(title = "Built With")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "Jetpack Compose", "Clean Architecture", "MVVM",
                "Hilt", "Retrofit", "Room", "Coroutines",
                "Paging 3", "WorkManager", "DataStore",
                "Firebase", "GitHub Actions"
            ).forEach { tech ->
                TechChip(label = tech)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text      = "Made with ❤️ in Qena, Egypt",
            fontSize  = 13.sp,
            color     = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier  = Modifier
                .fillMaxWidth()
                .padding(bottom = 110.dp)
        )
    }
}

@Composable
fun TechChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text       = label,
            fontSize   = 12.sp,
            color      = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(22.dp)
            )
        }

        Text(
            text       = label,
            fontSize   = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.weight(1f)
        )

        Text(
            text     = value,
            fontSize = 13.sp,
            color    = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun ProfileLinkItem(
    icon: Painter,
    label: String,
    value: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter        = icon,
                contentDescription = null,
                tint               = tint,
                modifier           = Modifier.size(22.dp)
            )
        }


        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = label,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text     = value,
                fontSize = 12.sp,
                color    = MaterialTheme.colorScheme.outline
            )
        }

        Icon(
            painter        = AppIcons.OpenInNew,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.outline,
            modifier           = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ProfileSectionHeader(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            bottom = 4.dp
        )
    )
}

@Preview
@Composable
fun ProfileScreenPreview(){
    NewsAppTheme() {
        ProfileScreen()
    }
}