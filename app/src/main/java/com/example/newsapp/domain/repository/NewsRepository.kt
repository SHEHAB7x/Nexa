package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getHeadlines(): Flow<Resource<List<Article>>>

    fun getArticlesByCategory(category: NewsCategory): Flow<Resource<List<Article>>>

    suspend fun saveArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    fun getSavedArticles(): Flow<List<Article>>
    fun isArticleSaved(url: String): Flow<Boolean>
}
