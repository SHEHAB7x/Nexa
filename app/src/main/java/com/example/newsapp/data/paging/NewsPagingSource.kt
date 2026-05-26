package com.example.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.mapper.dtosToArticles
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Country
import com.example.newsapp.domain.model.Language
import com.example.newsapp.domain.model.NewsCategory

class NewsPagingSource(
    private val api: NewsApiService,
    private val category: NewsCategory,
    private val language: String,
    private val country: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = when (category) {
                is NewsCategory.Apple   -> api.getAppleNews(
                    language = language,
                    page    = page,
                    pageSize = params.loadSize
                )
                is NewsCategory.Tesla      -> api.getTeslaNews(
                    language = language,
                    page     = page,
                    pageSize = params.loadSize
                )
                is NewsCategory.Business   -> api.getBusinessNews(
                    country  = country,
                    page     = page,
                    pageSize = params.loadSize
                )
                is NewsCategory.Wsj        -> api.getWsjNews(
                    language = language,
                    page     = page,
                    pageSize = params.loadSize
                )
                is NewsCategory.TechCrunch -> api.getTechCrunchNews(
                    page     = page,
                    pageSize = params.loadSize
                )
            }
            val articles = response.articles.dtosToArticles()

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}