package com.example.newsapp.presentation.navigation

sealed class Screen(val route: String) {
    object Home          : Screen("home")
    object Favorites     : Screen("favorites")
    object Search        : Screen("search")
    object ArticleDetail : Screen("article_detail/{articleUrl}") {
        fun createRoute(url: String): String =
            "article_detail/${java.net.URLEncoder.encode(url, "UTF-8")}"
    }
}