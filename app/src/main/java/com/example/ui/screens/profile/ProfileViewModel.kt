package com.example.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.preferences.UserPreferencesRepository
import com.example.data.repository.CalendarRepository
import com.example.data.repository.ProjectRepository
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CreatorStats(
    val totalProjects: Int = 0,
    val scriptCount: Int = 0,
    val ideaCount: Int = 0,
    val calendarCount: Int = 0
)

class ProfileViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val projectRepository: ProjectRepository,
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = preferencesRepository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    val stats: StateFlow<CreatorStats> = combine(
        projectRepository.totalCount,
        projectRepository.scriptCount,
        projectRepository.ideaCount,
        calendarRepository.totalCount
    ) { total, scripts, ideas, calendar ->
        CreatorStats(
            totalProjects = total,
            scriptCount = scripts,
            ideaCount = ideas,
            calendarCount = calendar
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreatorStats()
    )

    private val _isEditProfileOpen = MutableStateFlow(false)
    val isEditProfileOpen: StateFlow<Boolean> = _isEditProfileOpen.asStateFlow()

    fun openEditProfile() {
        _isEditProfileOpen.value = true
    }

    fun closeEditProfile() {
        _isEditProfileOpen.value = false
    }

    fun updateProfile(updated: UserProfile) {
        viewModelScope.launch {
            preferencesRepository.updateUserProfile(updated)
            closeEditProfile()
        }
    }
}
