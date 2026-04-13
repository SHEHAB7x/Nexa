package com.example.newsapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val rout: String,
    val icon: ImageVector,
    val label: String
) {
    object Home     : BottomNavItem(Screen.Home.route,      Icons.Outlined.Home,     "Home")
    object Favorites: BottomNavItem(Screen.Favorites.route, Icons.Outlined.Favorite, "Favorite")
}