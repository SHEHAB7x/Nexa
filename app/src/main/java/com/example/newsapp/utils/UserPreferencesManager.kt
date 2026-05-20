package com.example.newsapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newsapp.domain.model.Country
import com.example.newsapp.domain.model.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
){
    companion object{
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val DARK_MODE_ENABLED    = booleanPreferencesKey("dark_mode_enabled")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val SELECTED_LANGUAGE    = stringPreferencesKey("selected_language")
        private val SELECTED_COUNTRY     = stringPreferencesKey("selected_country")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
        }
    }

    val isDarkModeEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_MODE_ENABLED] ?: false }

    suspend fun setDarkModeEnabled(enabled: Boolean){
        context.dataStore.edit { it[DARK_MODE_ENABLED] = enabled }
    }

    val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[NOTIFICATIONS_ENABLED] ?: false }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }

    val selectedLanguage: Flow<Language> = context.dataStore.data
        .map { prefs ->
            val code = prefs[SELECTED_LANGUAGE] ?: Language.ENGLISH.code
            Language.fromCode(code)
        }

    suspend fun setLanguage(language: Language) {
        context.dataStore.edit { it[SELECTED_LANGUAGE] = language.code }
    }

    val selectedCountry: Flow<Country> = context.dataStore.data
        .map { prefs ->
            val code = prefs[SELECTED_COUNTRY] ?: Country.UNITED_STATES.code
            Country.fromCode(code)
        }

    suspend fun setCountry(country: Country) {
        context.dataStore.edit { it[SELECTED_COUNTRY] = country.code }
    }
}