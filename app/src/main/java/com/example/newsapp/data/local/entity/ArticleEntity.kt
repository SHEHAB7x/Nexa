package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String,
    val sourceName: String,
    val sourceId: String?,
    val author: String?,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String,
    val content: String?,
    val savedAt: Long = System.currentTimeMillis()
)

