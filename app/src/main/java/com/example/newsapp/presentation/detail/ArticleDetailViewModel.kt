package com.example.newsapp.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    val repository : NewsRepository
) : ViewModel() {

}