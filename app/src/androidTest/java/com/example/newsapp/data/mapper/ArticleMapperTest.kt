/*
package com.example.newsapp.data.mapper

import com.example.newsapp.data.remote.dto.ArticleDto
import com.example.newsapp.data.remote.dto.SourceDto
import org.junit.Assert
import org.junit.Test

class ArticleMapperTest {

    private val fakeDto = ArticleDto(
        source = SourceDto(id = "bbc", name = "BBC News"),
        author = "John Doe",
        title = "Test Title",
        description = "Test Description",
        url = "https://test.com",
        imageUrl = "https://image.com/img.jpg",
        publishedAt = "2026-04-07T10:00:00Z",
        content = "Full content [+1234 chars]"
    )

    @Test
    fun `toDomain maps correctly`() {
        val article = fakeDto.toDomain()
        Assert.assertNotNull(article)
        Assert.assertEquals("Test Title", article?.title)
        Assert.assertEquals("BBC News", article?.source?.name)
        Assert.assertEquals("bbc", article?.source?.id)
    }

    @Test
    fun `toDomain returns null when title is blank`() {
        val dto = fakeDto.copy(title = "")
        val article = dto.toDomain()
        Assert.assertNull(article)
    }

    @Test
    fun `toDomain strips chars suffix from content`() {
        val article = fakeDto.toDomain()
        Assert.assertFalse(article?.content?.contains("[+1234 chars]") == true)
    }

    @Test
    fun `toDomain returns null when title is null`() {
        val dto = fakeDto.copy(title = null)
        val article = dto.toDomain()
        Assert.assertNull(article)
    }

    @Test
    fun `dtosToArticles filters out null titles`() {
        val dtos = listOf(
            fakeDto,
            fakeDto.copy(title = null, url = "https://other.com"),
            fakeDto.copy(title = "Second", url = "https://second.com")
        )
        val articles = dtos.dtosToArticles()
        Assert.assertEquals(2, articles.size)
    }
}*/
