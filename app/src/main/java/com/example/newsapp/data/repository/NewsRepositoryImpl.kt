package com.example.newsapp.data.repository

import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.dao.CategoryArticleDao
import com.example.newsapp.data.local.dao.HeadlineDao
import com.example.newsapp.data.mapper.categoryEntitiesToArticles
import com.example.newsapp.data.mapper.dtosToArticles
import com.example.newsapp.data.mapper.entitiesToArticles
import com.example.newsapp.data.mapper.headlinesToArticles
import com.example.newsapp.data.mapper.toCategoryEntity
import com.example.newsapp.data.mapper.toEntity
import com.example.newsapp.data.mapper.toHeadlineEntity
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.util.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val articleDao: ArticleDao,
    private val headlineDao: HeadlineDao,
    private val categoryArticleDao: CategoryArticleDao
) : NewsRepository {

    override fun getHeadlines(
        country: String,
        language: String
    ): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        val cached = headlineDao.getHeadlines().first().headlinesToArticles()
        if(cached.isNotEmpty()){
            emit(Resource.Success(cached))
        }

        try {
            val business   = api.getBusinessNews().articles
            val techCrunch = api.getTechCrunchNews().articles
            val combined   = (business + techCrunch).dtosToArticles()

            headlineDao.clearHeadlines()
            headlineDao.insertAll(combined.map { it.toHeadlineEntity() })

            emit(Resource.Success(combined))
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().apply {
                setCustomKey("failed_call", "getHeadlines")
                recordException(e)
            }
            if (cached.isNotEmpty())
                emit(Resource.Error(e.message ?: "No internet connection"))
        }
    }

    override fun getArticlesByCategory(
        category: NewsCategory,
        language: String,
        country: String
    ): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())

        val cached = categoryArticleDao
            .getArticlesByCategory(category.label)
            .first()
            .categoryEntitiesToArticles()

        if (cached.isNotEmpty()){
            emit(Resource.Success(cached))
        }

        try {
            val fresh = when (category) {
                is NewsCategory.Apple      -> api.getAppleNews().articles
                is NewsCategory.Tesla      -> api.getTeslaNews().articles
                is NewsCategory.Business   -> api.getBusinessNews().articles
                is NewsCategory.Wsj        -> api.getWsjNews().articles
                is NewsCategory.TechCrunch -> api.getTechCrunchNews().articles
            }.dtosToArticles()

            categoryArticleDao.clearCategory(category.label)
            categoryArticleDao.insertAll(
                fresh.map { it.toCategoryEntity(category.label) }
            )
            emit(Resource.Success(fresh))
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().apply {
                setCustomKey("failed_call", "getArticlesByCategory")
                setCustomKey("category", category.label)
                recordException(e)
            }
            if (cached.isNotEmpty())
                emit(Resource.Error(e.message ?: "No internet connection"))
        }
    }

    override suspend fun saveArticle(article: Article) =
        articleDao.insertArticle(article.toEntity())

    override suspend fun deleteArticle(article: Article) =
        articleDao.deleteArticle(article.toEntity())

    override fun getSavedArticles(): Flow<List<Article>> =
        articleDao.getSavedArticles().map { it.entitiesToArticles() }

    override fun isArticleSaved(url: String): Flow<Boolean> =
        articleDao.isArticleSaved(url)
}
