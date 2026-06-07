package com.example.newsapp.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingHistoryViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ReadingHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHistory()
        observeReadCount()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }


            combine(
                repository.getReadArticleUrl(),
                repository.getCachedArticles()
            ) { readUrls, cachedArticles ->
                cachedArticles.filter { article ->
                    article.url in readUrls
                }
            }.collect { articles ->
                _uiState.update {
                    it.copy(
                        articles  = articles,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeReadCount() {
        viewModelScope.launch {
            repository.getReadCount().collect { count ->
                _uiState.update { it.copy(readCount = count) }
            }
        }
    }

    fun showClearDialog() {
        _uiState.update { it.copy(showClearDialog = true) }
    }

    fun dismissClearDialog() {
        _uiState.update { it.copy(showClearDialog = false) }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearReadHistory()
            _uiState.update { it.copy(showClearDialog = false) }
        }
    }
}