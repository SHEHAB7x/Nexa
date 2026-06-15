package com.example.newsapp.presentation.categorydetail
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.components.ArticleListItemShimmer
import com.example.newsapp.presentation.home.ArticleListItem

@Composable
fun CategoryDetailScreen(
    onArticleClick: (Article) -> Unit,
    onBackClick: () -> Unit,
    viewModel: CategoryDetailViewModel = hiltViewModel()
) {
    val articles = viewModel.articles.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

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
            Column {
                Text(
                    text       = viewModel.category.label,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text     = "All articles",
                    fontSize = 12.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            when (articles.loadState.refresh) {

                is LoadState.Loading -> {
                    items(8) {
                        ArticleListItemShimmer()
                        HorizontalDivider(
                            color    = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                is LoadState.Error -> {
                    item {
                        Box(
                            modifier         = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text  = "Failed to load articles",
                                    color = MaterialTheme.colorScheme.error
                                )
                                TextButton(
                                    onClick = { articles.retry() }
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }

                else -> {
                    items(
                        count = articles.itemCount,
                        key   = articles.itemKey { it.url }
                    ) { index ->
                        val article = articles[index]
                        article?.let {
                            ArticleListItem(
                                article = it,
                                onClick = { onArticleClick(it) }
                            )
                            HorizontalDivider(
                                color    = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    when (articles.loadState.append) {
                        is LoadState.Loading -> {
                            items(2) { ArticleListItemShimmer() }
                        }
                        is LoadState.Error -> {
                            item {
                                Box(
                                    modifier         = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextButton(
                                        onClick = { articles.retry() }
                                    ) {
                                        Text(
                                            text  = "Load more failed — Retry",
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}