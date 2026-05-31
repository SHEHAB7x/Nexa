package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.ReadHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markAsRead(entity: ReadHistoryEntity)

    @Query("SELECT url FROM read_history")
    fun getAllReadUrls(): Flow<List<String>>

    @Query("DELETE FROM read_history")
    suspend fun clearHistory()

    @Query("SELECT COUNT(*) FROM read_history")
    fun getReadCount(): Flow<Int>
}