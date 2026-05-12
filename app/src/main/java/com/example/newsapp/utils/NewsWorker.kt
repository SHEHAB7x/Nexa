package com.example.newsapp.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsapp.domain.model.NewsCategory
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class NewsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NewsRepository,
    private val notificationManager: NewsNotificationManager
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            val result = repository
                .getArticlesByCategory(NewsCategory.Business)
                .first { it !is Resource.Loading }

            if (result is Resource.Success) {
                val latest = result.data?.firstOrNull()
                latest?.let {
                    notificationManager.showBreakingNewsNotification(
                        title = it.title,
                        description = it.description ?: it.source.name
                    )
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

}