package com.example.ui.screens.studio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AfricaCreatorTopBar
import com.example.ui.components.CountrySelectorDialog
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.ToolCard

data class StudioToolItem(
    val emoji: String,
    val name: String,
    val desc: String,
    val tag: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val activeTool by viewModel.activeTool.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showCountryDialog by remember { mutableStateOf(false) }

    if (showCountryDialog) {
        CountrySelectorDialog(
            currentCountryName = userProfile.country,
            onDismiss = { showCountryDialog = false },
            onCountrySelected = { _, _ -> showCountryDialog = false }
        )
    }

    if (activeTool != null) {
        StudioToolDialog(
            toolName = activeTool!!,
            viewModel = viewModel,
            uiState = uiState,
            onDismiss = { viewModel.closeTool() }
        )
    }

    val allTools = listOf(
        StudioToolItem("🎬", "Idée de vidéo", "Génère des concepts originaux adaptés à ton style et ton pays", "Viral", "Vidéo & Audio"),
        StudioToolItem("🔥", "Hook Generator", "Accroches percutantes pour captiver en moins de 3 secondes", "Essentiel", "Vidéo & Audio"),
        StudioToolItem("✍️", "Script Generator", "Script complet structuré : Intro, Développement, Conclusion & CTA", "Complet", "Écriture"),
        StudioToolItem("🎥", "Prompt Vidéo", "Prompt détaillé optimisé pour Sora, Runway, Kling et Pika (9:16)", "IA Vidéo", "IA Visuelle"),
        StudioToolItem("🖼️", "Prompt Image", "Prompt haute définition pour Midjourney, Flux et DALL-E", "IA Photo", "IA Visuelle"),
        StudioToolItem("📢", "Ad Maker", "Publicités percutantes pour booster les ventes de commerces locaux", "Business", "Monétisation"),
        StudioToolItem("📖", "Story Maker", "Histoires captivantes inspirées des réalités africaines", "Storytelling", "Écriture"),
        StudioToolItem("#️⃣", "Hashtags Tendance", "Sélection de tags viraux et ciblés selon ta niche", "Croissance", "Croissance")
    )

    Scaffold(
        topBar = {
            AfricaCreatorTopBar(
                title = "Studio de Création",
                selectedCountryName = userProfile.country,
                onCountryClick = { showCountryDialog = true },
                actions = {
                    DemoModeBadge(modifier = Modifier.padding(end = 12.dp))
                }
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Boîte à outils du créateur 🧰",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tous les outils pour concevoir, structurer et démultiplier ton contenu",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(allTools.chunked(2)) { rowTools ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowTools.forEach { tool ->
                        ToolCard(
                            emoji = tool.emoji,
                            title = tool.name,
                            description = tool.desc,
                            tag = tool.tag,
                            onClick = { viewModel.openTool(tool.name) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowTools.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
