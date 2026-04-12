package com.example.newsapp.domain.model

sealed class NewsCategory(val label: String) {
    object Apple      : NewsCategory("Apple")
    object Tesla      : NewsCategory("Tesla")
    object Business   : NewsCategory("Business")
    object Wsj        : NewsCategory("WSJ")
    object TechCrunch : NewsCategory("TechCrunch")
}

