package com.example.newsapp.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.components.EmptyStateView
import com.example.newsapp.presentation.home.ArticleListItem
import com.example.newsapp.presentation.theme.*

@Composable
fun ReadingHistoryScreen(
    onArticleClick: (Article) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ReadingHistoryViewModel = hiltViewModel()
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
            verticalAlignment     = Alignment.CenterVertically,
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Reading History",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                // show count so user knows how many articles they read
                if (uiState.readCount > 0) {
                    Text(
                        text     = "${uiState.readCount} articles read",
                        fontSize = 12.sp,
                        color    = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // clear all button — only show when there is history
            if (uiState.articles.isNotEmpty()) {
                IconButton(onClick = viewModel::showClearDialog) {
                    Icon(
                        imageVector        = Icons.Default.Info,
                        contentDescription = "Clear history",
                        tint               = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

        // ── Content ──
        when {
            uiState.isLoading -> {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            uiState.articles.isEmpty() -> {
                EmptyStateView(
                    emoji    = "📖",
                    title    = "No reading history",
                    subtitle = "Articles you open will\nappear here"
                )
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = uiState.articles,
                        key   = { it.url }
                    ) { article ->
                        ArticleListItem(
                            article = article,
                            isRead  = true,    // all items here are read by definition
                            onClick = { onArticleClick(article) }
                        )
                        HorizontalDivider(
                            color    = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }

    // ── Clear confirmation dialog ──
    // we always ask before deleting — never delete without confirmation
    if (uiState.showClearDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissClearDialog,
            icon = {
                Icon(
                    imageVector        = Icons.Default.Info, // Delete sweep
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text       = "Clear History",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text  = "This will permanently delete your reading history. This action cannot be undone.",
                    color = MaterialTheme.colorScheme.outline
                )
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::clearHistory,
                    colors  = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text       = "Clear",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissClearDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}