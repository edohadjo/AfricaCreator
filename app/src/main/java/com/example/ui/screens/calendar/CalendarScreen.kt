package com.example.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.CalendarEntry
import com.example.domain.model.ContentStatus
import com.example.ui.components.AfricaCreatorTopBar
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.EmptyState
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier
) {
    val allEntries by viewModel.allEntries.collectAsStateWithLifecycle()
    val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val isAddDialogOpen by viewModel.isAddDialogOpen.collectAsStateWithLifecycle()
    val selectedEntry by viewModel.selectedEntry.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initDemoCalendar()
    }

    if (isAddDialogOpen) {
        AddEditCalendarDialog(
            initialEntry = selectedEntry,
            onDismiss = { viewModel.closeAddDialog() },
            onSave = { id, title, dateIso, timeStr, platform, contentType, status, notes ->
                viewModel.saveEntry(id, title, dateIso, timeStr, platform, contentType, status, notes)
            },
            onDelete = { id -> viewModel.deleteEntry(id) }
        )
    }

    Scaffold(
        topBar = {
            AfricaCreatorTopBar(
                title = "Calendrier de Contenu",
                actions = {
                    DemoModeBadge(modifier = Modifier.padding(end = 12.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_calendar_entry_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Planifier un contenu")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // View Mode Tab (Liste vs Calendrier)
            TabRow(
                selectedTabIndex = if (viewMode == CalendarViewMode.LIST) 0 else 1,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = viewMode == CalendarViewMode.LIST,
                    onClick = { viewModel.setViewMode(CalendarViewMode.LIST) },
                    text = { Text("Vue Liste (${allEntries.size})") },
                    icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = viewMode == CalendarViewMode.CALENDAR,
                    onClick = { viewModel.setViewMode(CalendarViewMode.CALENDAR) },
                    text = { Text("Vue Calendrier") },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            if (viewMode == CalendarViewMode.CALENDAR) {
                // Horizontal 14-days date selector
                val today = remember { LocalDate.now() }
                val daysList = remember { (0..13).map { today.plusDays(it.toLong()) } }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(daysList) { date ->
                        val isSelected = date == selectedDate
                        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.FRENCH).take(3).uppercase()
                        val dayNum = date.dayOfMonth.toString()
                        val hasEntries = allEntries.any { it.dateIso == date.toString() }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                )
                                .clickable { viewModel.setSelectedDate(date) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = dayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dayNum,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            if (hasEntries) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    }
                }
            }

            val displayedEntries = if (viewMode == CalendarViewMode.LIST) {
                allEntries
            } else {
                val iso = selectedDate.toString()
                allEntries.filter { it.dateIso == iso }
            }

            if (displayedEntries.isEmpty()) {
                EmptyState(
                    emoji = "📅",
                    title = "Aucune publication planifiée",
                    description = if (viewMode == CalendarViewMode.CALENDAR) "Rien de prévu pour ce jour. Clique sur '+' pour ajouter une publication !"
                    else "Ton calendrier est vide. Planifie tes publications à l'avance pour garder le rythme !",
                    actionText = "+ Planifier un contenu",
                    onActionClick = { viewModel.openAddDialog(null) }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedEntries, key = { it.id }) { entry ->
                        CalendarEntryCard(
                            entry = entry,
                            onEdit = { viewModel.openAddDialog(entry) },
                            onStatusChange = { newStatus -> viewModel.updateStatus(entry, newStatus) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarEntryCard(
    entry: CalendarEntry,
    onEdit: () -> Unit,
    onStatusChange: (ContentStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var showStatusMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📅 ${entry.dateIso} • ⏰ ${entry.timeStr}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${entry.platform.icon} ${entry.platform.displayName}",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                Box {
                    Surface(
                        color = Color(entry.status.colorHex).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showStatusMenu = true }
                    ) {
                        Text(
                            text = "${entry.status.label} ▾",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(entry.status.colorHex)
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        ContentStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    showStatusMenu = false
                                    onStatusChange(st)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (entry.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.notes,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier", modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
