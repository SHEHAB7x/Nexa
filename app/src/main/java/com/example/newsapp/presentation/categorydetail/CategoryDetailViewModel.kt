package com.example.newsapp.presentation.categorydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NewsRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {
    private val categoryLabel: String =
        checkNotNull(savedStateHandle["categoryLabel"])

    val category: NewsCategory = when (categoryLabel) {
        "Apple"      -> NewsCategory.Apple
        "Tesla"      -> NewsCategory.Tesla
        "Business"   -> NewsCategory.Business
        "WSJ"        -> NewsCategory.Wsj
        "TechCrunch" -> NewsCategory.TechCrunch
        else         -> NewsCategory.Apple
    }


    val articles: Flow<PagingData<Article>> = combine(
        preferencesManager.selectedLanguage,
        preferencesManager.selectedCountry
    ) { language, country ->
        language.code to country.code
    }.flatMapLatest { (language, country) ->
        repository.getPagedArticles(
            category = category,
            language = language,
            country  = country
        )
    }.cachedIn(viewModelScope)
}