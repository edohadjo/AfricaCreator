package com.example.ui.screens.studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AdResult
import com.example.ai.AiService
import com.example.ai.ImagePromptResult
import com.example.ai.ScriptResult
import com.example.ai.StoryResult
import com.example.ai.VideoPromptResult
import com.example.data.local.preferences.UserPreferencesRepository
import com.example.data.repository.ProjectRepository
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

sealed interface StudioUiState {
    data object Idle : StudioUiState
    data object Loading : StudioUiState
    data class IdeasSuccess(val ideas: List<String>) : StudioUiState
    data class HooksSuccess(val hooks: List<String>) : StudioUiState
    data class ScriptSuccess(val script: ScriptResult) : StudioUiState
    data class VideoPromptSuccess(val result: VideoPromptResult) : StudioUiState
    data class ImagePromptSuccess(val result: ImagePromptResult) : StudioUiState
    data class AdSuccess(val result: AdResult) : StudioUiState
    data class StorySuccess(val result: StoryResult) : StudioUiState
    data class HashtagsSuccess(val hashtags: List<String>) : StudioUiState
    data class Error(val message: String) : StudioUiState
}

class StudioViewModel(
    private val aiService: AiService,
    private val projectRepository: ProjectRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = preferencesRepository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    private val _uiState = MutableStateFlow<StudioUiState>(StudioUiState.Idle)
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    private val _activeTool = MutableStateFlow<String?>(null)
    val activeTool: StateFlow<String?> = _activeTool.asStateFlow()

    fun openTool(toolName: String) {
        _activeTool.value = toolName
        _uiState.value = StudioUiState.Idle
    }

    fun closeTool() {
        _activeTool.value = null
        _uiState.value = StudioUiState.Idle
    }

    fun generateVideoIdeas(
        topic: String,
        platform: Platform,
        theme: String,
        duration: String,
        style: String
    ) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val list = aiService.generateIdeas(
                    topic = topic,
                    platform = platform,
                    theme = theme,
                    duration = duration,
                    style = style,
                    country = userProfile.value.country,
                    city = userProfile.value.city,
                    language = userProfile.value.language
                )
                _uiState.value = StudioUiState.IdeasSuccess(list)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur de génération")
            }
        }
    }

    fun generateHooks(topic: String, style: String, platform: Platform) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val list = aiService.generateHooks(
                    topic = topic,
                    style = style,
                    platform = platform,
                    country = userProfile.value.country,
                    language = userProfile.value.language
                )
                _uiState.value = StudioUiState.HooksSuccess(list)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun generateScript(
        topic: String,
        duration: String,
        platform: Platform,
        tone: String
    ) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val script = aiService.generateScript(
                    topic = topic,
                    duration = duration,
                    platform = platform,
                    tone = tone,
                    country = userProfile.value.country,
                    language = userProfile.value.language
                )
                _uiState.value = StudioUiState.ScriptSuccess(script)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun generateVideoPrompt(
        character: String,
        appearance: String,
        environment: String,
        action: String,
        camera: String,
        lighting: String,
        movement: String,
        style: String
    ) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val res = aiService.generateVideoPrompt(
                    character, appearance, environment, action, camera, lighting, movement, style
                )
                _uiState.value = StudioUiState.VideoPromptSuccess(res)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun generateImagePrompt(
        subject: String,
        character: String,
        scenery: String,
        clothing: String,
        pose: String,
        expression: String,
        lighting: String,
        composition: String,
        cameraLens: String,
        style: String,
        format: String
    ) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val res = aiService.generateImagePrompt(
                    subject, character, scenery, clothing, pose, expression,
                    lighting, composition, cameraLens, style, format
                )
                _uiState.value = StudioUiState.ImagePromptSuccess(res)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun generateAdvertisement(
        productName: String,
        price: String,
        description: String,
        targetAudience: String,
        contact: String,
        location: String
    ) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val res = aiService.generateAdvertisement(
                    productName, price, description, targetAudience, contact, location
                )
                _uiState.value = StudioUiState.AdSuccess(res)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun generateStory(idea: String) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val res = aiService.generateStory(idea, userProfile.value.country)
                _uiState.value = StudioUiState.StorySuccess(res)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun generateHashtags(topic: String, platform: Platform) {
        viewModelScope.launch {
            _uiState.value = StudioUiState.Loading
            try {
                val tags = aiService.generateHashtags(topic, platform, userProfile.value.country)
                _uiState.value = StudioUiState.HashtagsSuccess(tags)
            } catch (e: Exception) {
                _uiState.value = StudioUiState.Error(e.localizedMessage ?: "Erreur")
            }
        }
    }

    fun saveContentAsProject(
        title: String,
        type: ContentType,
        preview: String,
        content: String,
        platform: Platform = Platform.TIKTOK
    ) {
        viewModelScope.launch {
            val project = CreatorProject(
                title = title,
                type = type,
                preview = preview,
                content = content,
                platform = platform,
                country = userProfile.value.country
            )
            projectRepository.insertProject(project)
        }
    }
}
