package com.example.newsapp.data.repository

import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.mapper.dtosToArticles
import com.example.newsapp.data.mapper.entitiesToArticles
import com.example.newsapp.data.mapper.toEntity
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val dao: ArticleDao
) : NewsRepository {

    override fun getHeadlines(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val business   = api.getBusinessNews().articles
            val techCrunch = api.getTechCrunchNews().articles
            val combined   = (business + techCrunch).dtosToArticles()
            emit(Resource.Success(combined))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load headlines"))
        }
    }

    override fun getArticlesByCategory(
        category: NewsCategory
    ): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val articles = when (category) {
                is NewsCategory.Apple      -> api.getAppleNews().articles
                is NewsCategory.Tesla      -> api.getTeslaNews().articles
                is NewsCategory.Business   -> api.getBusinessNews().articles
                is NewsCategory.Wsj        -> api.getWsjNews().articles
                is NewsCategory.TechCrunch -> api.getTechCrunchNews().articles
            }.dtosToArticles()
            emit(Resource.Success(articles))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load articles"))
        }
    }

    override suspend fun saveArticle(article: Article) =
        dao.insertArticle(article.toEntity())

    override suspend fun deleteArticle(article: Article) =
        dao.deleteArticle(article.toEntity())

    override fun getSavedArticles(): Flow<List<Article>> =
        dao.getSavedArticles().map { it.entitiesToArticles() }

    override fun isArticleSaved(url: String): Flow<Boolean> =
        dao.isArticleSaved(url)
}
