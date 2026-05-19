package com.example.newsapp.presentation.onboarding

data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String
)

val onboardingPage = listOf(
    OnboardingPage(
        title = "Stay Informed",
        description = "Get the latest news from around the world, updated every hour across all major categories.",
        emoji       = "🌍"
    ),
    OnboardingPage(
        title       = "Personalize Your Feed",
        description = "Choose your favorite topics — Technology, Business, Sports and more. Your news, your way.",
        emoji       = "🎯"
    ),
    OnboardingPage(
        title       = "Read Offline",
        description = "Save articles to your favorites and read them anytime, even without an internet connection.",
        emoji       = "📱"
    )
)
