package com.example.newsapp.presentation.favorites

import com.example.newsapp.domain.model.Article

data class FavoritesUiState(
    val articles : List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)