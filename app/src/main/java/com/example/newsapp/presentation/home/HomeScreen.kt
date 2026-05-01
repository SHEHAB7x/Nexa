package com.example.newsapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.model.Source
import com.example.newsapp.presentation.theme.*

@Composable
fun HomeScreen(
    onArticleClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState        = uiState,
        onArticleClick = onArticleClick,
        onSearchClick  = onSearchClick,
        onCategorySelected = viewModel::onCategorySelected
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onArticleClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategorySelected: (NewsCategory) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { SearchBarRow(onSearchClick = onSearchClick) }
        item {
            SectionHeader(
                title    = "Latest News",
                onSeeAll = {}
            )
        }
        item {
            if (uiState.isHeadlinesLoading) {
                Box(
                    modifier         = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.headlines) { article ->
                        HeadlineCard(
                            article = article,
                            onClick = { onArticleClick(article.url) }
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        category   = category,
                        isSelected = uiState.selectedCategory == category,
                        onClick    = { onCategorySelected(category) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (uiState.isCategoryLoading) {
            item {
                Box(
                    modifier         = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
        } else {
            items(uiState.categoryArticles) { article ->
                ArticleListItem(
                    article = article,
                    onClick = { onArticleClick(article.url) }
                )
                HorizontalDivider(
                    color     = LightGray,
                    thickness = 1.dp,
                    modifier  = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
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
            onCategorySelected = {}
        )
    }
}

@Composable
fun SearchBarRow(onSearchClick: () -> Unit) {
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
                .background(LightGray)
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
                    tint               = MediumGray,
                    modifier           = Modifier.size(20.dp)
                )
                Text(
                    text  = "Search...",
                    color = MediumGray,
                    fontSize = 14.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.Notifications,
                contentDescription = "Notification",
                tint               = White,
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
            color      = Black
        )
        Text(
            text      = "See All →",
            color     = Secondary,
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
                    .background(Color.Black.copy(alpha = 0.45f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text       = article.source.name,
                    color      = Color.White.copy(alpha = 0.8f),
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text       = article.title,
                    color      = Color.White,
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
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Primary else LightGray)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text      = category.label,
            color     = if (isSelected) White else DarkGray,
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
        ){
            AsyncImage(
                model              = article.imageUrl,
                contentDescription = article.title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )
            Text(
                modifier = Modifier.padding(15.dp),
                text = article.title,
                color = White,
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
                        color = White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = article.publishedAt.take(10),
                    color = White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

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