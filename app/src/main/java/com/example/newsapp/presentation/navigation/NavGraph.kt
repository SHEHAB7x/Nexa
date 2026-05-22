package com.example.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.presentation.SharedArticleViewModel
import com.example.newsapp.presentation.detail.ArticleDetailScreen
import com.example.newsapp.presentation.favorites.FavoritesScreen
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.onboarding.OnboardingScreen
import com.example.newsapp.presentation.search.SearchScreen
import com.example.newsapp.presentation.settings.SettingsScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
    ) {
    val sharedViewModel : SharedArticleViewModel = hiltViewModel()

    NavHost(
        navController    = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },

        exitTransition = {
            slideOutHorizontally (
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },

        popEnterTransition = {
            slideInHorizontally (
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },

        popExitTransition = {
            slideOutHorizontally (
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {

        composable(Screen.Onboarding.route){
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onArticleClick = { article ->
                    sharedViewModel.setArticle(article)
                    navController.navigate(Screen.ArticleDetails.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Settings.route){
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ArticleDetails.route) {
            val article by sharedViewModel.selectedArticle.collectAsState()
            ArticleDetailScreen(
                article = article,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onArticleClick = { article ->
                    sharedViewModel.setArticle(article)
                    navController.navigate(Screen.ArticleDetails.route)
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onArticleClick = {
                    sharedViewModel.setArticle(article = it)
                    navController.navigate(Screen.ArticleDetails.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}