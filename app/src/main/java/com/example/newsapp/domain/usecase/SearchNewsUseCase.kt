package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

}
