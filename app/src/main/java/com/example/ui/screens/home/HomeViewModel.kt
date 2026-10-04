package com.example.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiService
import com.example.ai.QuickPackResult
import com.example.data.local.preferences.UserPreferencesRepository
import com.example.data.repository.ProjectRepository
import com.example.domain.model.AfricanCountries
import com.example.domain.model.AfricanCountry
import com.example.domain.model.ContentType
import com.example.domain.model.CreatorProject
import com.example.domain.model.Platform
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object Loading : HomeUiState
    data class Success(val pack: QuickPackResult) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(
    private val aiService: AiService,
    private val projectRepository: ProjectRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = preferencesRepository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    val recentProjects: StateFlow<List<CreatorProject>> = projectRepository.allProjects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _ideaInput = MutableStateFlow("")
    val ideaInput: StateFlow<String> = _ideaInput.asStateFlow()

    private val _selectedPlatform = MutableStateFlow(Platform.TIKTOK)
    val selectedPlatform: StateFlow<Platform> = _selectedPlatform.asStateFlow()

    fun onIdeaChange(newIdea: String) {
        _ideaInput.value = newIdea
    }

    fun onPlatformSelect(platform: Platform) {
        _selectedPlatform.value = platform
    }

    fun generateQuickPack() {
        val idea = _ideaInput.value.trim()
        val currentCountry = userProfile.value.country
        val currentPlatform = _selectedPlatform.value

        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val result = aiService.generateQuickPack(
                    idea = idea,
                    country = currentCountry,
                    platform = currentPlatform
                )
                _uiState.value = HomeUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Une erreur est survenue")
            }
        }
    }

    fun saveFullPack(pack: QuickPackResult) {
        viewModelScope.launch {
            val project = CreatorProject(
                title = "Pack Créateur : ${pack.originalIdea.take(45)}...",
                type = ContentType.QUICK_PACK,
                preview = "Hooks : ${pack.hooks.firstOrNull() ?: ""} \nScript : ${pack.script.intro}",
                content = """
                    # PACK CRÉATEUR : ${pack.originalIdea}
                    
                    🎯 HOOKS D'ACCROCHE :
                    ${pack.hooks.mapIndexed { i, h -> "${i + 1}. $h" }.joinToString("\n")}
                    
                    🎬 SCRIPT COMPLET :
                    ${pack.script.fullFormattedText}
                    
                    🎥 PROMPT VIDÉO (9:16) :
                    ${pack.videoPrompt}
                    
                    🖼️ PROMPT IMAGE :
                    ${pack.imagePrompt}
                    
                    ⏱️ DÉCOUPAGE DES SCÈNES :
                    ${pack.scenes.joinToString("\n")}
                    
                    #️⃣ HASHTAGS RECOMMANDÉS :
                    ${pack.hashtags.joinToString(" ")}
                """.trimIndent(),
                platform = _selectedPlatform.value,
                country = userProfile.value.country,
                tags = "QuickPack, IA"
            )
            projectRepository.insertProject(project)
        }
    }

    fun updateCountry(country: AfricanCountry, city: String) {
        viewModelScope.launch {
            preferencesRepository.setSelectedCountry(country.name, city)
        }
    }

    fun resetState() {
        _uiState.value = HomeUiState.Idle
    }
}
