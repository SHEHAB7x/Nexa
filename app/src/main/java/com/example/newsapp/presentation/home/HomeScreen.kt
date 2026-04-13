package com.example.newsapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.presentation.theme.*

@Composable
fun HomeScreen(
    onArticleClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            SearchBarRow(onSearchClick = onSearchClick)
        }

        item {
            SectionHeader(
                title    = "Latest News",
                onSeeAll = {}
            )
        }

        item {
            if (uiState.isHeadlinesLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                LazyRow(
                    contentPadding    = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.headlines) { article ->
                        HeadlineCard(
                            article  = article,
                            onClick  = { onArticleClick(article.url) }
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
                        category    = category,
                        isSelected  = uiState.selectedCategory == category,
                        onClick     = { viewModel.onCategorySelected(category) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.isCategoryLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
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
                Divider(
                    color     = LightGray,
                    thickness = 1.dp,
                    modifier  = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
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
                    text  = "Dogecoin to the Moon...",
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
                imageVector        = Icons.Default.Search,
                contentDescription = "Filter",
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
            .width(260.dp)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.Top
    ) {
        AsyncImage(
            model              = article.imageUrl,
            contentDescription = article.title,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text       = article.source.name,
                color      = Primary,
                fontSize   = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text       = article.title,
                color      = Black,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis
            )
            article.description?.let {
                Text(
                    text     = it,
                    color    = MediumGray,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text     = article.publishedAt.take(10),
                color    = MediumGray,
                fontSize = 11.sp
            )
        }
    }
}