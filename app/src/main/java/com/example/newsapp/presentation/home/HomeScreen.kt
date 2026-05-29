package com.example.newsapp.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.model.Source
import com.example.newsapp.presentation.components.ArticleListItemShimmer
import com.example.newsapp.presentation.components.HeadlineCardShimmer
import com.example.newsapp.presentation.theme.*
import com.example.newsapp.utils.ReadTimeCalculator

@Composable
fun HomeScreen(
    onArticleClick: (Article) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState        = uiState,
        onArticleClick = onArticleClick,
        onSearchClick  = onSearchClick,
        onSettingsClick = onSettingsClick,
        onCategorySelected = viewModel::onCategorySelected,
        onRefresh = viewModel::refresh
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onArticleClick: (Article) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCategorySelected: (NewsCategory) -> Unit,
    onRefresh: () -> Unit
) {
    val isRefreshing = uiState.isHeadlinesLoading || uiState.isCategoryLoading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { SearchBarRow(onSearchClick = onSearchClick, onSettingsClick = onSettingsClick) }
            item {
                SectionHeader(
                    title = "Latest News",
                    onSeeAll = {}
                )
            }
            item {
                if (uiState.isHeadlinesLoading && uiState.headlines.isEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(3) {
                            HeadlineCardShimmer()
                        }
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.headlines) { article ->
                            HeadlineCard(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = uiState.selectedCategory == category,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.isCategoryLoading && uiState.categoryArticles.isEmpty()) {
                    items(5) {
                        ArticleListItemShimmer()
                        HorizontalDivider(
                            color    = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
            } else {
                items(uiState.categoryArticles) { article ->
                    ArticleListItem(
                        article = article,
                        onClick = { onArticleClick(article) }
                    )
                    HorizontalDivider(
                        color     = MaterialTheme.colorScheme.surfaceVariant,
                        thickness = 1.dp,
                        modifier  = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            uiState.categoryError?.let { error ->
                item {
                    Text(
                        text     = error,
                        color    = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun SearchBarRow(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onSearchClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment      = Alignment.CenterVertically,
                horizontalArrangement  = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.Search,
                    contentDescription = "Search",
                    tint               = MaterialTheme.colorScheme.outline,
                    modifier           = Modifier.size(20.dp)
                )
                Text(
                    text  = "Search news...",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 14.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onSettingsClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.Notifications,
                contentDescription = "Notification",
                tint               = MaterialTheme.colorScheme.background,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = title,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text      = "See All →",
            color     = MaterialTheme.colorScheme.secondary,
            fontSize  = 14.sp,
            modifier  = Modifier.clickable { onSeeAll() }
        )
    }
}

@Composable
fun HeadlineCard(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model             = article.imageUrl,
                contentDescription = article.title,
                contentScale      = ContentScale.Crop,
                modifier          = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text       = article.source.name,
                    color      = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text       = article.title,
                    color      = MaterialTheme.colorScheme.background,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 3,
                    overflow   = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: NewsCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "chip background"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White
                      else MaterialTheme.colorScheme.onBackground,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "chip text"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text      = category.label,
            color     = textColor,
            fontSize  = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ArticleListItem(
    article: Article,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val readTime = remember (article.url){
        ReadTimeCalculator.calculate(article.content, article.description)
    }

    AnimatedVisibility(
        visible = isVisible,
        enter   = fadeIn(animationSpec = tween(400)) +
                slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec  = tween(400)
                )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f))
                )
                Text(
                    modifier = Modifier.padding(15.dp),
                    text = article.title,
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    article.author?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = article.publishedAt.take(10),
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = ".",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 11.sp
                    )
                    Text(
                        text = readTime,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    NewsAppTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                headlines = listOf(fakeArticle, fakeArticle, fakeArticle),
                categoryArticles = listOf(
                    fakeArticle,
                    fakeArticle,
                    fakeArticle,
                    fakeArticle
                ),
                selectedCategory   = NewsCategory.Apple,
                isHeadlinesLoading = false,
                isCategoryLoading  = false
            ),
            onArticleClick     = {},
            onSearchClick      = {},
            onCategorySelected = {},
            onRefresh = {},
            onSettingsClick = {},
            categoryArticles = null
        )
    }
}*/

private val fakeArticle = Article(
    source      = Source(id = null, name = "BBC News"),
    author      = "John Doe",
    title       = "Crypto investors should be prepared to lose all their money",
    description = "The Bank of England governor says investors should be ready.",
    url         = "https://example.com",
    imageUrl    = null,
    publishedAt = "2026-04-07",
    content     = null,
    isSaved     = false
)