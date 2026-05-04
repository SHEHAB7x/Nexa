package com.example.newsapp.presentation.search

import androidx.lifecycle.ViewModel
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    repository: NewsRepository
) : ViewModel() {

}