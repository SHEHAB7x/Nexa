package com.example.newsapp.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.domain.model.Country
import com.example.newsapp.domain.model.Language
import com.example.newsapp.presentation.theme.*

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ── Top bar ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector        = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint               = MaterialTheme.colorScheme.onBackground,
                modifier           = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            Text(
                text       = "Settings",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            // ── Appearance section ──
            item { SettingsSectionHeader(title = "Appearance") }

            item {
                SettingsToggleItem(
                    icon        = Icons.Default.Build,
                    title       = "Dark Mode",
                    subtitle    = "Switch between light and dark theme",
                    isChecked   = uiState.isDarkMode,
                    onToggle    = viewModel::toggleDarkMode
                )
            }

            // ── Notifications section ──
            item { SettingsSectionHeader(title = "Notifications") }

            item {
                SettingsToggleItem(
                    icon        = Icons.Default.Notifications,
                    title       = "Breaking News Alerts",
                    subtitle    = "Get notified about the latest headlines",
                    isChecked   = uiState.isNotificationsEnabled,
                    onToggle    = viewModel::toggleNotifications
                )
            }

            // ── Content section ──
            item { SettingsSectionHeader(title = "Content") }

            item {
                SettingsClickableItem(
                    icon     = Icons.Default.Face,
                    title    = "News Language",
                    subtitle = "${uiState.selectedLanguage.flag}  ${uiState.selectedLanguage.displayName}",
                    onClick  = viewModel::showLanguageDialog
                )
            }

            item {
                SettingsClickableItem(
                    icon     = Icons.Default.ShoppingCart,
                    title    = "News Country",
                    subtitle = "${uiState.selectedCountry.flag}  ${uiState.selectedCountry.displayName}",
                    onClick  = viewModel::showCountryDialog
                )
            }

            // ── About section ──
            item { SettingsSectionHeader(title = "About") }

            item {
                SettingsInfoItem(
                    icon     = Icons.Default.Info,
                    title    = "App Version",
                    value    = "1.0.0"
                )
            }

            item {
                SettingsInfoItem(
                    icon     = Icons.Default.Person,
                    title    = "Developer",
                    value    = "Shehab Abdelhares"
                )
            }
        }
    }

    // ── Language dialog ──
    if (uiState.showLanguageDialog) {
        SelectionDialog(
            title       = "Select Language",
            items       = Language.values().map { "${it.flag}  ${it.displayName}" to it },
            selectedItem = uiState.selectedLanguage,
            onSelect    = { viewModel.selectLanguage(it) },
            onDismiss   = viewModel::dismissDialogs
        )
    }

    // ── Country dialog ──
    if (uiState.showCountryDialog) {
        SelectionDialog(
            title       = "Select Country",
            items       = Country.values().map { "${it.flag}  ${it.displayName}" to it },
            selectedItem = uiState.selectedCountry,
            onSelect    = { viewModel.selectCountry(it) },
            onDismiss   = viewModel::dismissDialogs
        )
    }
}

// ─────────────────────────────────────────
// Components
// ─────────────────────────────────────────

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text     = title,
        color    = MaterialTheme.colorScheme.primary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start  = 16.dp,
            top    = 20.dp,
            bottom = 4.dp
        )
    )
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isChecked) }
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = title,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text     = subtitle,
                fontSize = 12.sp,
                color    = MaterialTheme.colorScheme.outline
            )
        }
        Switch(
            checked         = isChecked,
            onCheckedChange = onToggle,
            colors          = SwitchDefaults.colors(
                checkedThumbColor  = MaterialTheme.colorScheme.primary,
                checkedTrackColor  = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = title,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text     = subtitle,
                fontSize = 12.sp,
                color    = MaterialTheme.colorScheme.outline
            )
        }
        Icon(
            imageVector        = Icons.Default.ShoppingCart,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.outline,
            modifier           = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsInfoItem(
    icon: ImageVector,
    title: String,
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
            text       = title,
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
fun <T> SelectionDialog(
    title: String,
    items: List<Pair<String, T>>,
    selectedItem: T,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape  = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text       = title,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground,
                    modifier   = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 8.dp
                    )
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                LazyColumn(
                    modifier       = Modifier.heightIn(max = 400.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items) { (label, item) ->
                        val isSelected = item == selectedItem
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(item) }
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text       = label,
                                fontSize   = 15.sp,
                                color      = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onBackground,
                                fontWeight = if (isSelected) FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector        = Icons.Default.Check,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.primary,
                                    modifier           = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}