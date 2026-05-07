package com.example.newsapp.presentation.search

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory

data class SearchUiState(
    val query: String = "",
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: NewsCategory? = null,
    val showFilterSheet: Boolean = false
)