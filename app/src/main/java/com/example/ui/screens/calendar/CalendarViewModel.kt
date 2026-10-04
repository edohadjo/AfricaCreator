package com.example.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CalendarRepository
import com.example.domain.model.CalendarEntry
import com.example.domain.model.ContentStatus
import com.example.domain.model.ContentType
import com.example.domain.model.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class CalendarViewMode {
    LIST,
    CALENDAR
}

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _viewMode = MutableStateFlow(CalendarViewMode.LIST)
    val viewMode: StateFlow<CalendarViewMode> = _viewMode.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedEntry = MutableStateFlow<CalendarEntry?>(null)
    val selectedEntry: StateFlow<CalendarEntry?> = _selectedEntry.asStateFlow()

    private val _isAddDialogOpen = MutableStateFlow(false)
    val isAddDialogOpen: StateFlow<Boolean> = _isAddDialogOpen.asStateFlow()

    val allEntries: StateFlow<List<CalendarEntry>> = calendarRepository.allEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val entriesForSelectedDate: StateFlow<List<CalendarEntry>> = combine(
        allEntries,
        _selectedDate
    ) { entries, date ->
        val iso = date.toString()
        entries.filter { it.dateIso == iso }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setViewMode(mode: CalendarViewMode) {
        _viewMode.value = mode
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun openAddDialog(entry: CalendarEntry? = null) {
        _selectedEntry.value = entry
        _isAddDialogOpen.value = true
    }

    fun closeAddDialog() {
        _selectedEntry.value = null
        _isAddDialogOpen.value = false
    }

    fun saveEntry(
        id: Long,
        title: String,
        dateIso: String,
        timeStr: String,
        platform: Platform,
        contentType: ContentType,
        status: ContentStatus,
        notes: String
    ) {
        viewModelScope.launch {
            val entry = CalendarEntry(
                id = id,
                dateIso = dateIso,
                timeStr = timeStr,
                title = title,
                platform = platform,
                contentType = contentType,
                status = status,
                notes = notes
            )
            if (id == 0L) {
                calendarRepository.insertEntry(entry)
            } else {
                calendarRepository.updateEntry(entry)
            }
            closeAddDialog()
        }
    }

    fun updateStatus(entry: CalendarEntry, newStatus: ContentStatus) {
        viewModelScope.launch {
            calendarRepository.updateEntry(entry.copy(status = newStatus))
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            calendarRepository.deleteEntryById(id)
            closeAddDialog()
        }
    }

    fun initDemoCalendar() {
        viewModelScope.launch {
            calendarRepository.initializeDemoCalendar()
        }
    }
}
