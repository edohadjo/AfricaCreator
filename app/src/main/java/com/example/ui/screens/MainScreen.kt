package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.components.AfricaCreatorBottomBar
import com.example.ui.navigation.BottomTab
import com.example.ui.screens.calendar.CalendarScreen
import com.example.ui.screens.calendar.CalendarViewModel
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.ProfileViewModel
import com.example.ui.screens.projects.ProjectsScreen
import com.example.ui.screens.projects.ProjectsViewModel
import com.example.ui.screens.studio.StudioScreen
import com.example.ui.screens.studio.StudioViewModel

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    studioViewModel: StudioViewModel,
    projectsViewModel: ProjectsViewModel,
    calendarViewModel: CalendarViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(BottomTab.HOME) }

    // Custom back handler to return to HOME tab if not already on HOME
    BackHandler(enabled = currentTab != BottomTab.HOME) {
        currentTab = BottomTab.HOME
    }

    Scaffold(
        bottomBar = {
            AfricaCreatorBottomBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentTab, label = "TabCrossfade") { tab ->
                when (tab) {
                    BottomTab.HOME -> {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToTool = { toolName ->
                                studioViewModel.openTool(toolName)
                                currentTab = BottomTab.STUDIO
                            },
                            onNavigateToProjects = {
                                currentTab = BottomTab.PROJECTS
                            },
                            onNavigateToSettings = onNavigateToSettings
                        )
                    }
                    BottomTab.STUDIO -> {
                        StudioScreen(
                            viewModel = studioViewModel
                        )
                    }
                    BottomTab.PROJECTS -> {
                        ProjectsScreen(
                            viewModel = projectsViewModel,
                            onNavigateToStudio = {
                                currentTab = BottomTab.STUDIO
                            }
                        )
                    }
                    BottomTab.CALENDAR -> {
                        CalendarScreen(
                            viewModel = calendarViewModel
                        )
                    }
                    BottomTab.PROFILE -> {
                        ProfileScreen(
                            viewModel = profileViewModel,
                            onNavigateToSettings = onNavigateToSettings
                        )
                    }
                }
            }
        }
    }
}
