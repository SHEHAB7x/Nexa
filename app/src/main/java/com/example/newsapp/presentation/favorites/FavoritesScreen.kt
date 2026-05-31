package com.example.newsapp.presentation.favorites

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.components.EmptyStateView
import com.example.newsapp.presentation.home.ArticleListItem
import com.example.newsapp.presentation.theme.DarkGray
import com.example.newsapp.presentation.theme.LightGray
import com.example.newsapp.presentation.theme.MediumGray
import com.example.newsapp.presentation.theme.Primary
import com.example.newsapp.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text       = "Favorites",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text     = "${uiState.articles.size} articles",
                fontSize = 13.sp,
                color    = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

        when {
            uiState.isLoading -> {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            uiState.articles.isEmpty() -> {
                EmptyStateView(
                    emoji    = "🤍",
                    title    = "No favorites yet",
                    subtitle = "Tap the heart icon on any article\nto save it here for later"
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
                        SwipeToDeleteItem(
                            onDelete = { viewModel.deleteArticle(article) }
                        ) {
                            ArticleListItem(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                        HorizontalDivider(
                            color    = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state            = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = when (dismissState.dismissDirection) {
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else                              -> Color.Transparent
                },
                label = "swipe color"
            )
            Box(
                modifier         = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector        = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint               = MaterialTheme.colorScheme.background,
                    modifier           = Modifier.size(28.dp)
                )
            }
        }
    ) {
        content()
    }
}
