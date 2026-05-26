package com.example.newsapp.domain.repository

import androidx.paging.PagingData
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Language
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getHeadlines(
        country: String = "us",
        language: String = "en"
    ): Flow<Resource<List<Article>>>

    fun getPagedArticles(
        category: NewsCategory,
        language: String = "en",
        country: String = "us"
    ): Flow<PagingData<Article>>

    fun getArticlesByCategory(
        category: NewsCategory,
        language: String = "en",
        country: String = "us"
    ): Flow<Resource<List<Article>>>

    suspend fun saveArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    fun getSavedArticles(): Flow<List<Article>>
    fun isArticleSaved(url: String): Flow<Boolean>
}
