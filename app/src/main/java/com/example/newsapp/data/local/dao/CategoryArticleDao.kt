package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.CategoryArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<CategoryArticleEntity>)

    @Query("SELECT * FROM category_articles WHERE category = :category ORDER BY cachedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<CategoryArticleEntity>>

    @Query("DELETE FROM category_articles WHERE category = :category")
    suspend fun clearCategory(category: String)
}