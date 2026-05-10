package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.dao.CategoryArticleDao
import com.example.newsapp.data.local.dao.HeadlineDao
import com.example.newsapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS headlines(
            url TEXT NOT NULL PRIMARY KEY,
                sourceName TEXT NOT NULL,
                sourceId TEXT,
                author TEXT,
                title TEXT NOT NULL,
                description TEXT,
                imageUrl TEXT,
                publishedAt TEXT NOT NULL,
                content TEXT,
                cachedAt INTEGER NOT NULL DEFAULT 0
                )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS category_articles (
                url TEXT NOT NULL PRIMARY KEY,
                category TEXT NOT NULL,
                sourceName TEXT NOT NULL,
                sourceId TEXT,
                author TEXT,
                title TEXT NOT NULL,
                description TEXT,
                imageUrl TEXT,
                publishedAt TEXT NOT NULL,
                content TEXT,
                cachedAt INTEGER NOT NULL DEFAULT 0
            )
        """)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideArticleDao(database: AppDatabase): ArticleDao =
        database.articleDao()

    @Provides
    @Singleton
    fun provideHeadlineDao(database: AppDatabase): HeadlineDao =
        database.headlineDao()

    @Provides
    @Singleton
    fun provideCategoryArticleDao(database: AppDatabase): CategoryArticleDao =
        database.categoryArticleDao()
}
