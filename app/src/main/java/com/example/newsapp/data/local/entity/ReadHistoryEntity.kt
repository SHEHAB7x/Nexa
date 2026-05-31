package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_history")
data class ReadHistoryEntity (
    @PrimaryKey
    val url: String,
    val readAt: Long = System.currentTimeMillis()
)