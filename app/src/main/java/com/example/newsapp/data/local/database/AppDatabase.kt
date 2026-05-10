package com.example.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.dao.CategoryArticleDao
import com.example.newsapp.data.local.dao.HeadlineDao
import com.example.newsapp.data.local.entity.ArticleEntity
import com.example.newsapp.data.local.entity.CategoryArticleEntity
import com.example.newsapp.data.local.entity.HeadlineEntity

@Database(
    entities = [
        ArticleEntity::class,
        HeadlineEntity::class,
        CategoryArticleEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun headlineDao(): HeadlineDao
    abstract fun categoryArticleDao(): CategoryArticleDao

    companion object{
        const val DATABASE_NAME = "news_db"
    }
}
