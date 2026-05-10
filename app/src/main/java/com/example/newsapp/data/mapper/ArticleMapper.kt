package com.example.newsapp.data.mapper

import com.example.newsapp.data.local.entity.ArticleEntity
import com.example.newsapp.data.local.entity.CategoryArticleEntity
import com.example.newsapp.data.local.entity.HeadlineEntity
import com.example.newsapp.data.remote.dto.ArticleDto
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Source

fun ArticleDto.toDomain(): Article? {
    if (title.isNullOrBlank()) return null
    if (url.isBlank()) return null

    return Article(
        source = Source(
            id   = source.id,
            name = source.name
        ),
        author      = author?.trim()?.ifBlank { null },
        title       = title.trim(),
        description = description?.trim()?.ifBlank { null },
        url         = url,
        imageUrl    = imageUrl,
        publishedAt = publishedAt,
        content     = content
            ?.replace(Regex("\\[\\+\\d+ chars\\]"), "")
            ?.trim()
            ?.ifBlank { null }
    )
}

fun List<ArticleDto>.dtosToArticles(): List<Article> =
    mapNotNull { it.toDomain() }
        .distinctBy { it.url }

fun Article.toEntity(): ArticleEntity =
    ArticleEntity(
        url         = url,
        sourceName  = source.name,
        sourceId    = source.id,
        author      = author,
        title       = title,
        description = description,
        imageUrl    = imageUrl,
        publishedAt = publishedAt,
        content     = content
    )

fun ArticleEntity.toDomain(): Article =
    Article(
        source = Source(
            id   = sourceId,
            name = sourceName
        ),
        author      = author,
        title       = title,
        description = description,
        url         = url,
        imageUrl    = imageUrl,
        publishedAt = publishedAt,
        content     = content,
        isSaved     = true
    )

fun List<ArticleEntity>.entitiesToArticles(): List<Article> =
    map { it.toDomain() }

fun Article.toHeadlineEntity(): HeadlineEntity =
    HeadlineEntity(
        url = url,
        sourceName = source.name,
        sourceId = source.id,
        author = author,
        title = title,
        description = description,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        content = content
    )
fun HeadlineEntity.toDomain(): Article =
    Article(
        source      = Source(id = sourceId, name = sourceName),
        author      = author,
        title       = title,
        description = description,
        url         = url,
        imageUrl    = imageUrl,
        publishedAt = publishedAt,
        content     = content
    )

fun List<HeadlineEntity>.headlinesToArticles(): List<Article> =
    map { it.toDomain() }

fun Article.toCategoryEntity(category: String): CategoryArticleEntity =
    CategoryArticleEntity(
        url         = url,
        category    = category,
        sourceName  = source.name,
        sourceId    = source.id,
        author      = author,
        title       = title,
        description = description,
        imageUrl    = imageUrl,
        publishedAt = publishedAt,
        content     = content
    )

fun CategoryArticleEntity.toDomain(): Article =
    Article(
        source      = Source(id = sourceId, name = sourceName),
        author      = author,
        title       = title,
        description = description,
        url         = url,
        imageUrl    = imageUrl,
        publishedAt = publishedAt,
        content     = content
    )

fun List<CategoryArticleEntity>.categoryEntitiesToArticles(): List<Article> =
    map { it.toDomain() }