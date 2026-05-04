package com.example.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsapp.presentation.SharedArticleViewModel
import com.example.newsapp.presentation.detail.ArticleDetailScreen
import com.example.newsapp.presentation.favorites.FavoritesScreen
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.search.SearchScreen
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
    ) {
    val sharedViewModel : SharedArticleViewModel = hiltViewModel()

    NavHost(
        navController    = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onArticleClick = { article ->
                    sharedViewModel.setArticle(article)
                    navController.navigate(Screen.ArticleDetails.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        composable(Screen.ArticleDetails.route) {
            val article by sharedViewModel.selectedArticle.collectAsState()
            ArticleDetailScreen(
                article = article,
                onBackClick = { navController.popBackStack() }
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

        */
    }
}