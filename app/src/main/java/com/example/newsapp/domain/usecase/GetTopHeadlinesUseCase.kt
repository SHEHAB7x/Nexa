package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor (private val repository: NewsRepository) {
    suspend operator fun invoke(): Flow<Resource<List<Article>>> {
        return repository.getHeadlines()
    }
}
