package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Main : Screen("main")
    data object Settings : Screen("settings")
    data object StudioTool : Screen("studio_tool/{toolType}") {
        fun createRoute(toolType: String) = "studio_tool/$toolType"
    }
}

enum class BottomTab(val route: String, val title: String, val iconName: String) {
    HOME("home", "Accueil", "home"),
    STUDIO("studio", "Studio", "studio"),
    PROJECTS("projects", "Projets", "projects"),
    CALENDAR("calendar", "Calendrier", "calendar"),
    PROFILE("profile", "Profil", "profile")
}
