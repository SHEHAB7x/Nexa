package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("SELECT * FROM saved_articles ORDER BY savedAt DESC")
    fun getSavedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE url = :url)")
    fun isArticleSaved(url: String): Flow<Boolean>

    @Query("SELECT * FROM saved_articles WHERE url = :url LIMIT 1")
    suspend fun getArticleByUrl(url: String): ArticleEntity?
}
