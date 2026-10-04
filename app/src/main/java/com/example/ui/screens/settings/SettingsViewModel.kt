package com.example.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.preferences.UserPreferencesRepository
import com.example.data.repository.CalendarRepository
import com.example.data.repository.ProjectRepository
import com.example.domain.model.UserProfile
import com.example.ui.components.shareText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val projectRepository: ProjectRepository,
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    val themeMode: StateFlow<String> = preferencesRepository.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "SYSTEM"
    )

    val userProfile: StateFlow<UserProfile> = preferencesRepository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    fun clearAllProjects() {
        viewModelScope.launch {
            projectRepository.deleteAll()
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            projectRepository.deleteAll()
            projectRepository.initializeDemoProjects()
            calendarRepository.deleteAll()
            calendarRepository.initializeDemoCalendar()
        }
    }

    fun exportData(context: Context) {
        viewModelScope.launch {
            val projects = projectRepository.allProjects.first()
            val calendar = calendarRepository.allEntries.first()
            val profile = userProfile.value

            val exportText = buildString {
                appendLine("=== EXPORTATION DONNÉES AFRICACREATOR ===")
                appendLine("Créateur : ${profile.name} (${profile.username})")
                appendLine("Pays : ${profile.country}")
                appendLine("Date : ${java.time.LocalDate.now()}")
                appendLine()
                appendLine("--- PROJETS SAUVEGARDÉS (${projects.size}) ---")
                projects.forEachIndexed { i, p ->
                    appendLine("${i + 1}. [${p.type.label}] ${p.title} (${p.platform.displayName})")
                    appendLine(p.content)
                    appendLine("----------------------------------------")
                }
                appendLine()
                appendLine("--- CALENDRIER (${calendar.size}) ---")
                calendar.forEachIndexed { i, c ->
                    appendLine("${i + 1}. ${c.dateIso} à ${c.timeStr} : ${c.title} [${c.status.label}]")
                }
            }

            shareText(context, exportText, "Exportation Données AfricaCreator")
        }
    }
}
