/*
package com.example.newsapp.data.repository

import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.dao.CategoryArticleDao
import com.example.newsapp.data.local.dao.HeadlineDao
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.data.remote.dto.ArticleDto
import com.example.newsapp.data.remote.dto.NewsResponseDto
import com.example.newsapp.data.remote.dto.SourceDto
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.collections.emptyList

class NewsRepositoryTest {

    private lateinit var repository: NewsRepositoryImpl
    private val api = mockk<NewsApiService>()
    private val articleDao = mockk<ArticleDao>(relaxed = true)
    private val headlineDao = mockk<HeadlineDao>(relaxed = true)
    private val categoryDao = mockk<CategoryArticleDao>(relaxed = true)

    private val fakeArticleDto = ArticleDto(
        source = SourceDto(id = null, name = "BBC"),
        author = "John",
        title = "Test Article",
        description = "Test description",
        url = "hhtps://test.com",
        imageUrl = null,
        publishedAt = "2026-04-07",
        content = "Test content"
    )

    private val fakeResponse = NewsResponseDto(
        status = "ok",
        totalResults = 1,
        articles = listOf(fakeArticleDto)
    )

    @Before
    fun setUp() {
        coEvery { headlineDao.getHeadlines() } returns flowOf(emptyList())
        coEvery { categoryDao.getArticlesByCategory(any()) } returns flowOf(emptyList())

        repository = NewsRepositoryImpl(api, articleDao, headlineDao, categoryDao)
    }
    @Test
    fun `getHeadlines emits loading then success`() = runTest {
        coEvery { api.getBusinessNews() }   returns fakeResponse
        coEvery { api.getTechCrunchNews() } returns fakeResponse

        repository.getHeadlines().test {
            val loading = awaitItem()
            assertTrue(loading is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getHeadlines emits error when API fails and cache is empty`() = runTest {
        coEvery { api.getBusinessNews() } throws Exception("No internet")

        repository.getHeadlines().test {
            awaitItem() // loading
            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals("No internet", error.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getArticlesByCategory returns correct articles`() = runTest {
        coEvery { api.getAppleNews() } returns fakeResponse

        repository.getArticlesByCategory(NewsCategory.Apple).test {
            awaitItem() // loading
            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(1, success.data?.size)
            assertEquals("Test Article", success.data?.first()?.title)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
*/
