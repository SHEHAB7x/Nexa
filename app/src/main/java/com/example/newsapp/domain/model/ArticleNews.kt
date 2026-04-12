package com.example.newsapp.domain.model

data class ArticleNews(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
