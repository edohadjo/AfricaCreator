package com.example.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ai.AiServiceImpl
import com.example.data.local.AfricaCreatorDatabase
import com.example.data.local.preferences.UserPreferencesRepository
import com.example.data.repository.CalendarRepository
import com.example.data.repository.ProjectRepository
import com.example.ui.navigation.Screen
import com.example.ui.screens.MainScreen
import com.example.ui.screens.calendar.CalendarViewModel
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileViewModel
import com.example.ui.screens.projects.ProjectsViewModel
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.settings.SettingsViewModel
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.studio.StudioViewModel
import com.example.ui.theme.AfricaCreatorTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AfricaCreatorApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val database = remember { AfricaCreatorDatabase.getInstance(context) }
    val preferencesRepository = remember { UserPreferencesRepository(context) }
    val projectRepository = remember { ProjectRepository(database.projectDao()) }
    val calendarRepository = remember { CalendarRepository(database.calendarDao()) }
    val aiService = remember { AiServiceImpl() }

    val themeMode by preferencesRepository.themeMode.collectAsStateWithLifecycle(initialValue = "SYSTEM")
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemDark
    }

    // Seed demo data on initial launch
    LaunchedEffect(Unit) {
        projectRepository.initializeDemoProjects()
        calendarRepository.initializeDemoCalendar()
    }

    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel {
        HomeViewModel(aiService, projectRepository, preferencesRepository)
    }
    val studioViewModel: StudioViewModel = viewModel {
        StudioViewModel(aiService, projectRepository, preferencesRepository)
    }
    val projectsViewModel: ProjectsViewModel = viewModel {
        ProjectsViewModel(projectRepository)
    }
    val calendarViewModel: CalendarViewModel = viewModel {
        CalendarViewModel(calendarRepository)
    }
    val profileViewModel: ProfileViewModel = viewModel {
        ProfileViewModel(preferencesRepository, projectRepository, calendarRepository)
    }
    val settingsViewModel: SettingsViewModel = viewModel {
        SettingsViewModel(preferencesRepository, projectRepository, calendarRepository)
    }

    AfricaCreatorTheme(darkTheme = isDark) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onNavigateNext = {
                            coroutineScope.launch {
                                val completed = preferencesRepository.isOnboardingCompleted.first()
                                if (completed) {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(Screen.Onboarding.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                    )
                }

                composable(Screen.Onboarding.route) {
                    OnboardingScreen(
                        onFinish = {
                            coroutineScope.launch {
                                preferencesRepository.setOnboardingCompleted(true)
                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                composable(Screen.Main.route) {
                    MainScreen(
                        homeViewModel = homeViewModel,
                        studioViewModel = studioViewModel,
                        projectsViewModel = projectsViewModel,
                        calendarViewModel = calendarViewModel,
                        profileViewModel = profileViewModel,
                        onNavigateToSettings = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
