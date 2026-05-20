package com.example.newsapp.domain.model

enum class Language(val code: String, val displayName: String, val flag: String) {
    ENGLISH("en", "English", "us"),
    ARABIC("ar",  "Arabic",   "🇪🇬"),
    FRENCH("fr",  "French",   "🇫🇷"),
    GERMAN("de",  "German",   "🇩🇪"),
    SPANISH("es", "Spanish",  "🇪🇸"),
    ITALIAN("it", "Italian",  "🇮🇹"),
    PORTUGUESE("pt", "Portuguese", "🇧🇷"),
    RUSSIAN("ru", "Russian",  "🇷🇺"),
    CHINESE("zh", "Chinese",  "🇨🇳"),
    JAPANESE("ja", "Japanese","🇯🇵");

    companion object {
        fun fromCode(code: String): Language =
            values().find { it.code == code } ?: ENGLISH
    }
}