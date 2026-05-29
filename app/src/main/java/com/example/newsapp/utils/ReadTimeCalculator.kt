package com.example.newsapp.utils

object ReadTimeCalculator {
    private const val WORDS_PER_MINUTE = 200
    fun calculate(content: String?, description: String?): String{
        val text = buildString {
            description.let { append(it) }
            content?.let { append(" $it") }
        }
        if (text.isBlank()) return "1 min read"
        val wordCount = text.trim().split("\\s+".toRegex()).size
        val minutes = maxOf(1, wordCount / WORDS_PER_MINUTE)
        return "$minutes min read"
    }
}