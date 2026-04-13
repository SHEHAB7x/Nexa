package com.example.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsapp.presentation.detail.ArticleDetailScreen
import com.example.newsapp.presentation.favorites.FavoritesScreen
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.search.SearchScreen
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
    ) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onArticleClick = { url ->
                    navController.navigate(Screen.ArticleDetail.createRoute(url))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        /*composable(Screen.Favorites.route) {
            FavoritesScreen(
                onArticleClick = { url ->
                    navController.navigate(Screen.ArticleDetail.createRoute(url))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onArticleClick = { url ->
                    navController.navigate(Screen.ArticleDetail.createRoute(url))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.ArticleDetail.route,
            arguments = listOf(
                navArgument("articleUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("articleUrl") ?: ""
            ArticleDetailScreen(
                articleUrl = url,
                onBackClick = { navController.popBackStack() }
            )
        }*/
    }
}