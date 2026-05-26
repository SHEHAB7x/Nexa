package com.example.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecase.GetTopHeadlinesUseCase
import com.example.newsapp.domain.util.Resource
import com.example.newsapp.utils.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    val categoryArticles: Flow<PagingData<Article>> =
        combine(
            preferencesManager.selectedLanguage,
            preferencesManager.selectedCountry,
            _uiState.map { it.selectedCategory }
        ) { language, country, category ->
            Triple(language, country, category)
        }
            .flatMapLatest { (language, country, category) ->
                repository.getPagedArticles(
                    category = category,
                    language = language.code,
                    country = country.code
                )
            }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.selectedLanguage,
                preferencesManager.selectedCountry
            ) { language, country ->
                language.code to country.code
            }.collect { (language, country) ->
                loadHeadlines(country, language)
                loadCategoryArticles(
                    _uiState.value.selectedCategory,
                    language,
                    country
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val language = preferencesManager.selectedLanguage.first().code
            val country = preferencesManager.selectedCountry.first().code
            loadHeadlines(country,language)
            loadCategoryArticles(
                _uiState.value.selectedCategory,
                language,
                country
            )
        }
    }

    private fun loadHeadlines(
        country: String,
        language: String
    ){
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

    private fun loadCategoryArticles(
        category: NewsCategory,
        language: String,
        country: String
    ) {
        viewModelScope.launch {
            repository.getArticlesByCategory(category,language,country).collect { result ->
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


    fun onCategorySelected(category: NewsCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        viewModelScope.launch {
            val language = preferencesManager.selectedLanguage.first().code
            val country  = preferencesManager.selectedCountry.first().code
            loadCategoryArticles(category, language, country)
        }
    }
}