package com.example.newsapp.presentation.navigation

sealed class Screen(val route: String) {
    object Onboarding    : Screen("onboarding")
    object Home          : Screen("home")
    object Favorites     : Screen("favorites")
    object Search        : Screen("search")
    object ArticleDetails : Screen("article_detail")
}