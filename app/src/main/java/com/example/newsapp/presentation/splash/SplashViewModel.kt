package com.example.newsapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.utils.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StartDestination {
    object Onboarding : StartDestination()
    object Home       : StartDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<StartDestination?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val isCompleted = preferencesManager.isOnboardingCompleted.first()
            _startDestination.value = if (isCompleted) {
                StartDestination.Home
            } else {
                StartDestination.Onboarding
            }
        }
    }
}