package com.example.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AfricaCreatorTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var showClearDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Effacer tous les projets ?") },
            text = { Text("Cette action supprimera définitivement tous tes projets créés.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllProjects()
                        showClearDialog = false
                        Toast.makeText(context, "Projets effacés", Toast.LENGTH_SHORT).show()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Effacer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Réinitialiser les données de démonstration ?") },
            text = { Text("Cela restaurera les 3 projets exemples et les entrées du calendrier de départ.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetDemoData()
                        showResetDialog = false
                        Toast.makeText(context, "Données démo restaurées ✨", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Réinitialiser")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    if (showThemeDialog) {
        val options = listOf("SYSTEM" to "Suivre le système", "LIGHT" to "Mode clair", "DARK" to "Mode sombre")
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Thème de l'application") },
            text = {
                Column {
                    options.forEach { (key, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.setThemeMode(key)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(selected = themeMode == key, onClick = {
                                viewModel.setThemeMode(key)
                                showThemeDialog = false
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) { Text("Fermer") }
            }
        )
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Langue de l'interface") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(selected = true, onClick = {})
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Français (Actif)")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(selected = false, onClick = {})
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("English (Bientôt disponible)")
                    }
                    Text(
                        text = "Prochaines langues régionales : Fon, Ewe, Yoruba, Hausa, Swahili, Lingala",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) { Text("OK") }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("À propos d'AfricaCreator") },
            text = {
                Column {
                    Text(text = "AfricaCreator Version 1.0", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "« Tes idées. Ton contenu. Ton audience. »")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "L'application mobile tout-en-un conçue spécialement pour donner du pouvoir aux créateurs de contenu africains. Fonctionne 100% hors-ligne avec le Mode Démo local.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("Fermer") }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Politique de Confidentialité") },
            text = {
                Text(
                    text = "AfricaCreator respecte ta vie privée. Toutes tes créations, scripts, calendrier et profils sont stockés localement sur ton appareil grâce à la base de données Room. Aucune donnée secrète ou personnelle n'est envoyée à des tiers sans ton consentement explicite.",
                    style = MaterialTheme.typography.bodySmall
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("Compris") }
            }
        )
    }

    Scaffold(
        topBar = {
            AfricaCreatorTopBar(
                title = "Paramètres",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Apparence
            item {
                SettingsSectionHeader("🎨 Apparence")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.DarkMode,
                            title = "Thème de l'affichage",
                            subtitle = when (themeMode) {
                                "LIGHT" -> "Mode clair"
                                "DARK" -> "Mode sombre"
                                else -> "Système"
                            },
                            onClick = { showThemeDialog = true }
                        )
                    }
                }
            }

            // Langue
            item {
                SettingsSectionHeader("🌐 Langue")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.Language,
                            title = "Langue de l'application",
                            subtitle = "Français",
                            onClick = { showLanguageDialog = true }
                        )
                    }
                }
            }

            // Données
            item {
                SettingsSectionHeader("💾 Données & Sauvegarde")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.Share,
                            title = "Exporter toutes mes données",
                            subtitle = "Partager ou sauvegarder mes projets et planning",
                            onClick = { viewModel.exportData(context) }
                        )
                        HorizontalDivider()
                        SettingsRow(
                            icon = Icons.Default.Refresh,
                            title = "Restaurer les exemples démo",
                            subtitle = "Recharger les 3 projets de démonstration",
                            onClick = { showResetDialog = true }
                        )
                        HorizontalDivider()
                        SettingsRow(
                            icon = Icons.Default.DeleteForever,
                            title = "Effacer tous mes projets",
                            subtitle = "Supprimer tout le contenu local",
                            titleColor = MaterialTheme.colorScheme.error,
                            onClick = { showClearDialog = true }
                        )
                    }
                }
            }

            // À propos
            item {
                SettingsSectionHeader("ℹ️ À propos")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.Info,
                            title = "AfricaCreator Version",
                            subtitle = "1.0 (Build Native Android)",
                            onClick = { showAboutDialog = true }
                        )
                        HorizontalDivider()
                        SettingsRow(
                            icon = Icons.Default.Lock,
                            title = "Politique de confidentialité",
                            subtitle = "Stockage 100% local et sécurisé",
                            onClick = { showPrivacyDialog = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold, color = titleColor))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}
