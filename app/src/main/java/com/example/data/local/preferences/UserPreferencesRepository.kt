package com.example.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.Platform
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "africacreator_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val THEME_MODE = stringPreferencesKey("theme_mode") // SYSTEM, LIGHT, DARK
        val SELECTED_COUNTRY = stringPreferencesKey("selected_country")
        val SELECTED_CITY = stringPreferencesKey("selected_city")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
        val CREATOR_NAME = stringPreferencesKey("creator_name")
        val CREATOR_USERNAME = stringPreferencesKey("creator_username")
        val CREATOR_BIO = stringPreferencesKey("creator_bio")
        val PREFERRED_PLATFORM = stringPreferencesKey("preferred_platform")
        val FAVORITE_THEME = stringPreferencesKey("favorite_theme")
        val AVATAR_EMOJI = stringPreferencesKey("avatar_emoji")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
    }

    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE] ?: "SYSTEM"
    }

    val selectedCountry: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SELECTED_COUNTRY] ?: "Côte d'Ivoire"
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data.map { preferences ->
        val platformStr = preferences[PreferencesKeys.PREFERRED_PLATFORM] ?: Platform.TIKTOK.name
        val safePlatform = runCatching { Platform.valueOf(platformStr) }.getOrDefault(Platform.TIKTOK)
        UserProfile(
            name = preferences[PreferencesKeys.CREATOR_NAME] ?: "Kouassi Créateur",
            username = preferences[PreferencesKeys.CREATOR_USERNAME] ?: "@kouassicreator",
            bio = preferences[PreferencesKeys.CREATOR_BIO] ?: "Créateur de contenu digital inspiré par l'Afrique 🚀",
            country = preferences[PreferencesKeys.SELECTED_COUNTRY] ?: "Côte d'Ivoire",
            city = preferences[PreferencesKeys.SELECTED_CITY] ?: "Abidjan",
            preferredPlatform = safePlatform,
            favoriteTheme = preferences[PreferencesKeys.FAVORITE_THEME] ?: "Business & Motivation",
            language = preferences[PreferencesKeys.SELECTED_LANGUAGE] ?: "Français",
            avatarEmoji = preferences[PreferencesKeys.AVATAR_EMOJI] ?: "🦁"
        )
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }

    suspend fun setSelectedCountry(country: String, city: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_COUNTRY] = country
            if (city != null) {
                preferences[PreferencesKeys.SELECTED_CITY] = city
            }
        }
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CREATOR_NAME] = profile.name
            preferences[PreferencesKeys.CREATOR_USERNAME] = profile.username
            preferences[PreferencesKeys.CREATOR_BIO] = profile.bio
            preferences[PreferencesKeys.SELECTED_COUNTRY] = profile.country
            preferences[PreferencesKeys.SELECTED_CITY] = profile.city
            preferences[PreferencesKeys.PREFERRED_PLATFORM] = profile.preferredPlatform.name
            preferences[PreferencesKeys.FAVORITE_THEME] = profile.favoriteTheme
            preferences[PreferencesKeys.SELECTED_LANGUAGE] = profile.language
            preferences[PreferencesKeys.AVATAR_EMOJI] = profile.avatarEmoji
        }
    }
}
