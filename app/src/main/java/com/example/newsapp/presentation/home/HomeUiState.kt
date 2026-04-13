package com.example.newsapp.presentation.home

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory

data class HomeUiState(
    val headlines: List<Article>       = emptyList(),
    val categoryArticles: List<Article> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.Apple,
    val isHeadlinesLoading: Boolean     = false,
    val isCategoryLoading: Boolean      = false,
    val headlinesError: String?         = null,
    val categoryError: String?          = null
)

val categories = listOf(
    NewsCategory.Apple,
    NewsCategory.Tesla,
    NewsCategory.Business,
    NewsCategory.Wsj,
    NewsCategory.TechCrunch
)
