package com.example.ui.screens.studio

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ContentType
import com.example.domain.model.Platform
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.ErrorState
import com.example.ui.components.LoadingState
import com.example.ui.components.PlatformSelector
import com.example.ui.components.ResultCard
import com.example.ui.components.copyToClipboard
import com.example.ui.components.shareText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StudioToolDialog(
    toolName: String,
    viewModel: StudioViewModel,
    uiState: StudioUiState,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = toolName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fermer")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
            ) {
                when (uiState) {
                    is StudioUiState.Loading -> {
                        LoadingState(message = "AfricaCreator génère ton contenu...")
                    }
                    is StudioUiState.Error -> {
                        ErrorState(message = uiState.message, onRetry = { /* will retry */ })
                    }
                    is StudioUiState.IdeasSuccess -> {
                        IdeasResultView(
                            ideas = uiState.ideas,
                            onSave = { idea ->
                                viewModel.saveContentAsProject(
                                    title = idea.take(50),
                                    type = ContentType.VIDEO_IDEA,
                                    preview = idea,
                                    content = idea
                                )
                                Toast.makeText(context, "Idée sauvegardée ! 🚀", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    is StudioUiState.HooksSuccess -> {
                        HooksResultView(
                            hooks = uiState.hooks,
                            onSave = { hook ->
                                viewModel.saveContentAsProject(
                                    title = "Hook : ${hook.take(45)}...",
                                    type = ContentType.HOOK,
                                    preview = hook,
                                    content = hook
                                )
                                Toast.makeText(context, "Hook sauvegardé ! 🔥", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    is StudioUiState.ScriptSuccess -> {
                        ScriptResultView(
                            script = uiState.script,
                            onSave = { updatedText ->
                                viewModel.saveContentAsProject(
                                    title = uiState.script.title,
                                    type = ContentType.SCRIPT,
                                    preview = uiState.script.intro,
                                    content = updatedText
                                )
                                Toast.makeText(context, "Script sauvegardé ! ✍️", Toast.LENGTH_SHORT).show()
                            },
                            onRegenerate = {
                                // re-trigger
                            }
                        )
                    }
                    is StudioUiState.VideoPromptSuccess -> {
                        VideoPromptResultView(
                            result = uiState.result,
                            onSave = {
                                viewModel.saveContentAsProject(
                                    title = uiState.result.title,
                                    type = ContentType.VIDEO_PROMPT,
                                    preview = uiState.result.character,
                                    content = uiState.result.fullPrompt
                                )
                                Toast.makeText(context, "Prompt vidéo sauvegardé ! 🎥", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    is StudioUiState.ImagePromptSuccess -> {
                        ImagePromptResultView(
                            result = uiState.result,
                            onSave = {
                                viewModel.saveContentAsProject(
                                    title = uiState.result.title,
                                    type = ContentType.IMAGE_PROMPT,
                                    preview = uiState.result.style,
                                    content = uiState.result.fullPrompt
                                )
                                Toast.makeText(context, "Prompt image sauvegardé ! 🖼️", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    is StudioUiState.AdSuccess -> {
                        AdResultView(
                            result = uiState.result,
                            onSave = {
                                viewModel.saveContentAsProject(
                                    title = "Pub : ${uiState.result.productName}",
                                    type = ContentType.AD_MAKER,
                                    preview = uiState.result.hook,
                                    content = uiState.result.fullFormattedText
                                )
                                Toast.makeText(context, "Publicité sauvegardée ! 📢", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    is StudioUiState.StorySuccess -> {
                        StoryResultView(
                            result = uiState.result,
                            onSave = {
                                viewModel.saveContentAsProject(
                                    title = uiState.result.title,
                                    type = ContentType.STORY,
                                    preview = uiState.result.beginning,
                                    content = uiState.result.fullFormattedText
                                )
                                Toast.makeText(context, "Histoire sauvegardée ! 📖", Toast.LENGTH_SHORT).show()
                            },
                            onTransformToVideo = {
                                viewModel.generateScript(
                                    topic = "Histoire : ${uiState.result.title}",
                                    duration = "60 secondes",
                                    platform = Platform.TIKTOK,
                                    tone = "Storytelling inspirant"
                                )
                            }
                        )
                    }
                    is StudioUiState.HashtagsSuccess -> {
                        HashtagsResultView(
                            hashtags = uiState.hashtags,
                            onSave = {
                                viewModel.saveContentAsProject(
                                    title = "Hashtags tendance",
                                    type = ContentType.HASHTAGS,
                                    preview = uiState.hashtags.take(5).joinToString(" "),
                                    content = uiState.hashtags.joinToString(" ")
                                )
                                Toast.makeText(context, "Hashtags sauvegardés ! #️⃣", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    StudioUiState.Idle -> {
                        // Show tool configuration form
                        ToolConfigForm(toolName = toolName, viewModel = viewModel)
                    }
                }
            }
        },
        confirmButton = {},
        shape = RoundedCornerShape(24.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolConfigForm(toolName: String, viewModel: StudioViewModel) {
    var topic by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf(Platform.TIKTOK) }
    var duration by remember { mutableStateOf("60 secondes") }
    var style by remember { mutableStateOf("Motivation") }
    var tone by remember { mutableStateOf("Inspirant") }

    // Ad Maker specific fields
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("10.000 FCFA") }
    var targetAudience by remember { mutableStateOf("Jeunes et actifs") }
    var contactInfo by remember { mutableStateOf("WhatsApp : +225 07 00 00 00") }

    val stylesList = listOf("Humour", "Motivation", "Storytelling", "Éducation", "Business", "Lifestyle", "Culture")
    val durationsList = listOf("30s", "60s", "90s", "3 min", "10 min")

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            DemoModeBadge()
            Spacer(modifier = Modifier.height(12.dp))
        }

        when {
            toolName.contains("Idée", ignoreCase = true) -> {
                item {
                    Text("Plateforme cible :", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    PlatformSelector(selectedPlatform = selectedPlatform, onPlatformSelected = { selectedPlatform = it })

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Thème ou niche") },
                        placeholder = { Text("Ex: Création de contenu, e-commerce, friperie...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Style de vidéo :", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        stylesList.forEach { s ->
                            FilterChip(
                                selected = style == s,
                                onClick = { style = s },
                                label = { Text(s) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            viewModel.generateVideoIdeas(
                                topic = topic,
                                platform = selectedPlatform,
                                theme = topic.ifBlank { "Entrepreneuriat" },
                                duration = duration,
                                style = style
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_generate_ideas")
                    ) {
                        Text("✨ Générer des Idées")
                    }
                }
            }

            toolName.contains("Hook", ignoreCase = true) -> {
                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Sujet de ta vidéo") },
                        placeholder = { Text("Ex: Pourquoi tes vidéos ne font pas de vues...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Plateforme :", style = MaterialTheme.typography.labelMedium)
                    PlatformSelector(selectedPlatform = selectedPlatform, onPlatformSelected = { selectedPlatform = it })

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Style d'accroche :", style = MaterialTheme.typography.labelMedium)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Mystère", "Choc", "Curiosité", "Conseil contre-intuitif").forEach { s ->
                            FilterChip(
                                selected = style == s,
                                onClick = { style = s },
                                label = { Text(s) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.generateHooks(topic, style, selectedPlatform) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_generate_hooks")
                    ) {
                        Text("🔥 Générer les Hooks")
                    }
                }
            }

            toolName.contains("Script", ignoreCase = true) -> {
                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Sujet du script") },
                        placeholder = { Text("Ex: 3 habitudes quotidiennes pour exploser son business") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Plateforme :", style = MaterialTheme.typography.labelMedium)
                    PlatformSelector(selectedPlatform = selectedPlatform, onPlatformSelected = { selectedPlatform = it })

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Durée estimée :", style = MaterialTheme.typography.labelMedium)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        durationsList.forEach { d ->
                            FilterChip(
                                selected = duration == d,
                                onClick = { duration = d },
                                label = { Text(d) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.generateScript(topic, duration, selectedPlatform, tone) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_generate_script")
                    ) {
                        Text("✍️ Générer le Script Complet")
                    }
                }
            }

            toolName.contains("Prompt vidéo", ignoreCase = true) -> {
                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Scène ou personnage") },
                        placeholder = { Text("Ex: Un jeune créateur filmant dans les rues de Dakar") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Format standard : 9:16 vertical (Sora, Runway, Pika, Kling)", style = MaterialTheme.typography.labelSmall)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.generateVideoPrompt(
                                character = topic,
                                appearance = "élégant, moderne, énergique",
                                environment = "ville africaine vibrante avec lumière dorée",
                                action = "regarde la caméra avec confiance et présente son innovation",
                                camera = "85mm lens, f/1.8, cinematic slow push",
                                lighting = "golden hour sunset glow, warm highlights",
                                movement = "smooth handheld tracking",
                                style = "hyper-realistic 8K commercial grade"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("🎥 Créer le Prompt Vidéo")
                    }
                }
            }

            toolName.contains("Prompt image", ignoreCase = true) -> {
                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Description de l'image souhaitée") },
                        placeholder = { Text("Ex: Portrait d'une jeune styliste africaine avec ses créations") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.generateImagePrompt(
                                subject = topic,
                                character = "jeune femme créative africaine",
                                scenery = "atelier de haute couture baigné de soleil",
                                clothing = "robe wax revisitée contemporaine",
                                pose = "fière et souriante",
                                expression = "chaleureuse et déterminée",
                                lighting = "lumière naturelle douce du matin",
                                composition = "règle des tiers, 85mm f/1.4",
                                cameraLens = "Sony A7R V",
                                style = "photographie éditoriale moderne",
                                format = "--ar 9:16 --v 6.0"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("🖼️ Créer le Prompt Image")
                    }
                }
            }

            toolName.contains("Publicité", ignoreCase = true) -> {
                item {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Nom du produit ou service") },
                        placeholder = { Text("Ex: Huile capillaire bio pousse rapide") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Prix / Offre") },
                        placeholder = { Text("Ex: 5.000 FCFA") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = targetAudience,
                        onValueChange = { targetAudience = it },
                        label = { Text("Public cible") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contactInfo,
                        onValueChange = { contactInfo = it },
                        label = { Text("Contact (WhatsApp / Téléphone)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.generateAdvertisement(
                                productName = productName,
                                price = price,
                                description = "Produit de qualité supérieure garanti",
                                targetAudience = targetAudience,
                                contact = contactInfo,
                                location = "Livraison locale et sous-région"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("📢 Générer le Pack Pub")
                    }
                }
            }

            toolName.contains("Story", ignoreCase = true) -> {
                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Idée de l'histoire") },
                        placeholder = { Text("Ex: L'histoire d'un apprenti mécanicien qui a conçu une moto solaire") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.generateStory(topic) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("📖 Écrire l'Histoire")
                    }
                }
            }

            else -> { // Hashtags
                item {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Thème de tes hashtags") },
                        placeholder = { Text("Ex: Mode africaine, gastronomie, investissement...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PlatformSelector(selectedPlatform = selectedPlatform, onPlatformSelected = { selectedPlatform = it })

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.generateHashtags(topic, selectedPlatform) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("#️⃣ Générer les Hashtags")
                    }
                }
            }
        }
    }
}

// Result views for tools
@Composable
fun IdeasResultView(ideas: List<String>, onSave: (String) -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            DemoModeBadge()
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(ideas) { idea ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = idea, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { copyToClipboard(context, idea, "Idée de vidéo") }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copier")
                        }
                        TextButton(onClick = { onSave(idea) }) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sauvegarder")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HooksResultView(hooks: List<String>, onSave: (String) -> Unit) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            DemoModeBadge()
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(hooks) { hook ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = hook, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Row {
                        IconButton(onClick = { copyToClipboard(context, hook, "Hook") }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copier", modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { onSave(hook) }) {
                            Icon(Icons.Default.Bookmark, contentDescription = "Sauvegarder", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScriptResultView(
    script: com.example.ai.ScriptResult,
    onSave: (String) -> Unit,
    onRegenerate: () -> Unit
) {
    var editableText by remember(script.fullFormattedText) { mutableStateOf(script.fullFormattedText) }

    ResultCard(
        title = script.title,
        content = editableText,
        isEditable = true,
        onContentChange = { editableText = it },
        onSave = { onSave(editableText) },
        onRegenerate = onRegenerate
    )
}

@Composable
fun VideoPromptResultView(
    result: com.example.ai.VideoPromptResult,
    onSave: () -> Unit
) {
    ResultCard(
        title = result.title,
        content = result.fullPrompt,
        onSave = onSave
    )
}

@Composable
fun ImagePromptResultView(
    result: com.example.ai.ImagePromptResult,
    onSave: () -> Unit
) {
    ResultCard(
        title = result.title,
        content = result.fullPrompt,
        onSave = onSave
    )
}

@Composable
fun AdResultView(
    result: com.example.ai.AdResult,
    onSave: () -> Unit
) {
    ResultCard(
        title = "Publicité : ${result.productName}",
        content = result.fullFormattedText,
        onSave = onSave
    )
}

@Composable
fun StoryResultView(
    result: com.example.ai.StoryResult,
    onSave: () -> Unit,
    onTransformToVideo: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        ResultCard(
            title = result.title,
            content = result.fullFormattedText,
            onSave = onSave
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onTransformToVideo,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎬 Transformer en vidéo (Script)")
        }
    }
}

@Composable
fun HashtagsResultView(
    hashtags: List<String>,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val allTags = remember(hashtags) { hashtags.joinToString(" ") }

    Column(modifier = Modifier.fillMaxWidth()) {
        ResultCard(
            title = "#️⃣ Hashtags Tendance",
            content = allTags,
            onSave = onSave
        )
    }
}
