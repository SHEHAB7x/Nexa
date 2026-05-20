package com.example.newsapp.presentation.settings

import com.example.newsapp.domain.model.Country
import com.example.newsapp.domain.model.Language
import com.example.newsapp.domain.model.Language.*

data class SettingsUiState(
    val isDarkMode: Boolean             = false,
    val isNotificationsEnabled: Boolean = true,
    val selectedLanguage: Language = ENGLISH,
    val selectedCountry: Country = Country.UNITED_STATES,
    val showLanguageDialog: Boolean     = false,
    val showCountryDialog: Boolean      = false
)
