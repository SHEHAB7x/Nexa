package com.example.newsapp.presentation.history

import com.example.newsapp.domain.model.Article

data class ReadingHistoryUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean      = false,
    val readCount: Int          = 0,
    val showClearDialog: Boolean = false
)
