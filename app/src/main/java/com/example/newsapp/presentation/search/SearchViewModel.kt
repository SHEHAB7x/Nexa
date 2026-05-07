package com.example.newsapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .filter { it.length >= 2 }
                .collect {
                    searchNews(it)
                }
        }
    }
    fun onQueryChanged(query: String){
        _searchQuery.value = query
        _uiState.update {
            it.copy(query = query)
        }
        if(query.isBlank()){
            _uiState.update { it.copy(articles = emptyList(), error = null) }
        }
    }

    fun onCategorySelected(category: NewsCategory){
        _uiState.update {
            it.copy(selectedCategory = if (it.selectedCategory == category) null else category)
        }
        searchNews(_uiState.value.query)
    }

    fun  toggleFilterSheet(){
        _uiState.update { it.copy(showFilterSheet = !it.showFilterSheet) }
    }

    fun dismissFilterSheet(){
        _uiState.update { it.copy(showFilterSheet = false) }
    }

    private fun searchNews(query: String) {
        if(query.isBlank()) return
        viewModelScope.launch {
            repository.getArticlesByCategory(
                _uiState.value.selectedCategory ?: NewsCategory.Apple
            ).collect { result ->
                when(result){
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        val filtered = result.data
                            ?.filter { article ->
                                article.title.contains(query, ignoreCase = true) ||
                                        article.description?.contains(query, ignoreCase = true) == true
                            } ?: emptyList()
                        _uiState.update {
                            it.copy(articles = filtered, isLoading = false)
                        }
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }
}