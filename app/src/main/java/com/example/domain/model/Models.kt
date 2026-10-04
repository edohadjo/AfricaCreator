package com.example.domain.model

data class CreatorProject(
    val id: Long = 0,
    val title: String,
    val type: ContentType,
    val dateEpoch: Long = System.currentTimeMillis(),
    val preview: String,
    val content: String,
    val platform: Platform = Platform.TIKTOK,
    val country: String = "Côte d'Ivoire",
    val isFavorite: Boolean = false,
    val tags: String = ""
)

data class CalendarEntry(
    val id: Long = 0,
    val dateIso: String, // YYYY-MM-DD
    val timeStr: String = "18:00",
    val title: String,
    val platform: Platform = Platform.TIKTOK,
    val contentType: ContentType = ContentType.VIDEO_IDEA,
    val status: ContentStatus = ContentStatus.TO_PREPARE,
    val notes: String = "",
    val linkedProjectId: Long? = null
)

data class UserProfile(
    val name: String = "Kouassi Créateur",
    val username: String = "@kouassicreator",
    val bio: String = "Créateur de contenu digital inspiré par l'Afrique 🚀",
    val country: String = "Côte d'Ivoire",
    val city: String = "Abidjan",
    val preferredPlatform: Platform = Platform.TIKTOK,
    val favoriteTheme: String = "Business & Motivation",
    val language: String = "Français",
    val avatarEmoji: String = "🦁"
)
