package com.example.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecase.GetTopHeadlinesUseCase
import com.example.newsapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHeadlines()
        loadCategoryArticles(NewsCategory.Apple)
    }

    private fun loadHeadlines(){
        viewModelScope.launch {
            getTopHeadlinesUseCase.invoke().collect { result ->
                when(result){
                    is Resource.Loading -> _uiState.update {
                        it.copy(isHeadlinesLoading = true, headlinesError = null)
                    }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            headlines = result.data ?: emptyList(),
                            isHeadlinesLoading = false
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isHeadlinesLoading = false,
                            headlinesError = result.message
                        )
                    }
                }
            }
        }
    }

    fun onCategorySelected(category: NewsCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadCategoryArticles(category)
    }

    private fun loadCategoryArticles(category: NewsCategory) {
        viewModelScope.launch {
            repository.getArticlesByCategory(category).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update {
                        it.copy(isCategoryLoading = true, categoryError = null)
                    }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            categoryArticles = result.data ?: emptyList(),
                            isCategoryLoading = false
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isCategoryLoading = false,
                            categoryError     = result.message
                        )
                    }
                }
            }
        }
    }

    fun refresh() {
        loadHeadlines()
        loadCategoryArticles(_uiState.value.selectedCategory)
    }
}