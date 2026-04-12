package com.example.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object{
        const val DATABASE_NAME = "news_db"
    }
}
