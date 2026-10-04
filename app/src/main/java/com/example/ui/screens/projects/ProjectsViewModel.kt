package com.example.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.ProjectRepository
import com.example.domain.model.ContentType
import com.example.domain.model.CreatorProject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ProjectFilter(val label: String, val contentType: ContentType?) {
    ALL("Tous", null),
    SCRIPTS("Scripts", ContentType.SCRIPT),
    VIDEOS("Vidéos", ContentType.VIDEO_PROMPT),
    IMAGES("Images", ContentType.IMAGE_PROMPT),
    STORIES("Stories", ContentType.STORY),
    ADS("Publicités", ContentType.AD_MAKER),
    IDEAS("Idées & Hooks", ContentType.VIDEO_IDEA)
}

class ProjectsViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(ProjectFilter.ALL)
    val selectedFilter: StateFlow<ProjectFilter> = _selectedFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedProject = MutableStateFlow<CreatorProject?>(null)
    val selectedProject: StateFlow<CreatorProject?> = _selectedProject.asStateFlow()

    val filteredProjects: StateFlow<List<CreatorProject>> = combine(
        projectRepository.allProjects,
        _selectedFilter,
        _searchQuery
    ) { projects, filter, query ->
        projects.filter { project ->
            val matchesFilter = when (filter) {
                ProjectFilter.ALL -> true
                ProjectFilter.SCRIPTS -> project.type == ContentType.SCRIPT
                ProjectFilter.VIDEOS -> project.type == ContentType.VIDEO_PROMPT || project.type == ContentType.VIDEO_IDEA
                ProjectFilter.IMAGES -> project.type == ContentType.IMAGE_PROMPT
                ProjectFilter.STORIES -> project.type == ContentType.STORY
                ProjectFilter.ADS -> project.type == ContentType.AD_MAKER
                ProjectFilter.IDEAS -> project.type == ContentType.VIDEO_IDEA || project.type == ContentType.HOOK
            }
            val matchesQuery = query.isBlank() ||
                    project.title.contains(query, ignoreCase = true) ||
                    project.content.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setFilter(filter: ProjectFilter) {
        _selectedFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectProject(project: CreatorProject?) {
        _selectedProject.value = project
    }

    fun deleteProject(project: CreatorProject) {
        viewModelScope.launch {
            projectRepository.deleteProjectById(project.id)
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = null
            }
        }
    }

    fun duplicateProject(project: CreatorProject) {
        viewModelScope.launch {
            val duplicate = project.copy(
                id = 0,
                title = "${project.title} (Copie)",
                dateEpoch = System.currentTimeMillis()
            )
            projectRepository.insertProject(duplicate)
        }
    }

    fun updateProjectContent(project: CreatorProject, newTitle: String, newContent: String) {
        viewModelScope.launch {
            val updated = project.copy(
                title = newTitle,
                content = newContent,
                preview = newContent.take(140)
            )
            projectRepository.updateProject(updated)
            _selectedProject.value = updated
        }
    }

    fun initDemoProjects() {
        viewModelScope.launch {
            projectRepository.initializeDemoProjects()
        }
    }
}
