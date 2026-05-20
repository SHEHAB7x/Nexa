package com.example.newsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.components.BottomNavBar
import com.example.newsapp.presentation.navigation.NavGraph
import com.example.newsapp.presentation.navigation.Screen
import com.example.newsapp.presentation.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import android.Manifest
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.presentation.settings.SettingsViewModel
import com.example.newsapp.presentation.splash.SplashViewModel
import com.example.newsapp.presentation.splash.StartDestination
import com.example.newsapp.utils.NetworkMonitor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val splashViewModel: SplashViewModel by viewModels()

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{
            splashViewModel.startDestination.value == null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
        setContent {
            val splashViewModel: SplashViewModel = hiltViewModel()
            val settingsViewModel: SettingsViewModel = hiltViewModel()


            val startDestination by splashViewModel.startDestination.collectAsState()
            val settingsUiState by settingsViewModel.uiState.collectAsState()

            startDestination?.let { destination ->

                val startRoute = when (destination) {
                    is StartDestination.Onboarding -> Screen.Onboarding.route
                    is StartDestination.Home       -> Screen.Home.route
                }
                NewsAppTheme(darkTheme = settingsUiState.isDarkMode) {


                    val navController     = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute      = navBackStackEntry?.destination?.route

                    val showBottomBar = currentRoute in listOf(
                        Screen.Home.route,
                        Screen.Favorites.route
                    )

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            NavGraph(
                                navController    = navController,
                                startDestination = startRoute
                            )
                        }
                    }
                }
            }
        }
    }
}