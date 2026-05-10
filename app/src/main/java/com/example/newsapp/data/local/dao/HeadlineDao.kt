package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.HeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadlineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<HeadlineEntity>)

    @Query("SELECT * FROM headlines ORDER BY cachedAt DESC")
    fun getHeadlines() : Flow<List<HeadlineEntity>>

    @Query("DELETE FROM headlines")
    suspend fun clearHeadlines()
}