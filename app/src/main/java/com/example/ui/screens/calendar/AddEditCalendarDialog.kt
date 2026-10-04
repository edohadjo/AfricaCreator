package com.example.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.CalendarEntry
import com.example.domain.model.ContentStatus
import com.example.domain.model.ContentType
import com.example.domain.model.Platform
import com.example.ui.components.PlatformSelector
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditCalendarDialog(
    initialEntry: CalendarEntry?,
    onDismiss: () -> Unit,
    onSave: (id: Long, title: String, dateIso: String, timeStr: String, platform: Platform, contentType: ContentType, status: ContentStatus, notes: String) -> Unit,
    onDelete: ((Long) -> Unit)? = null
) {
    var title by remember { mutableStateOf(initialEntry?.title ?: "") }
    var dateIso by remember { mutableStateOf(initialEntry?.dateIso ?: LocalDate.now().toString()) }
    var timeStr by remember { mutableStateOf(initialEntry?.timeStr ?: "18:00") }
    var platform by remember { mutableStateOf(initialEntry?.platform ?: Platform.TIKTOK) }
    var contentType by remember { mutableStateOf(initialEntry?.contentType ?: ContentType.VIDEO_IDEA) }
    var status by remember { mutableStateOf(initialEntry?.status ?: ContentStatus.TO_PREPARE) }
    var notes by remember { mutableStateOf(initialEntry?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (initialEntry == null) "📅 Planifier un Contenu" else "✏️ Modifier le Contenu",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fermer")
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titre de la publication") },
                        placeholder = { Text("Ex: Vidéo 3 erreurs business à Abidjan") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = dateIso,
                            onValueChange = { dateIso = it },
                            label = { Text("Date (YYYY-MM-DD)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = timeStr,
                            onValueChange = { timeStr = it },
                            label = { Text("Heure") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Plateforme :", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    PlatformSelector(selectedPlatform = platform, onPlatformSelected = { platform = it })

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Statut :", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ContentStatus.values().forEach { st ->
                            FilterChip(
                                selected = status == st,
                                onClick = { status = st },
                                label = { Text(st.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(st.colorHex).copy(alpha = 0.2f),
                                    selectedLabelColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes de préparation (idées, rushs...)") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (initialEntry != null && onDelete != null) {
                    IconButton(onClick = { onDelete(initialEntry.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.error)
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onSave(
                                initialEntry?.id ?: 0L,
                                title,
                                dateIso,
                                timeStr,
                                platform,
                                contentType,
                                status,
                                notes
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (initialEntry == null) "Ajouter" else "Enregistrer")
                }
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
