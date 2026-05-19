package com.example.newsapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.utils.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {
    fun completeOnboarding(onDone: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.setOnboardingCompleted()
            onDone()
        }
    }
}