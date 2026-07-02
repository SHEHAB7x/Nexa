package com.example.newsapp.presentation.detail

sealed class TtsState {
    object Initializing : TtsState()

    object Ready : TtsState()

    data class Playing(
        val progress: Float = 0f
    ) : TtsState()

    data class Error(val message: String) : TtsState()
}