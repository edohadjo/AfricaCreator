package com.example.ai

import com.example.domain.model.ContentType
import com.example.domain.model.Platform

data class ScriptResult(
    val title: String,
    val intro: String,
    val body: String,
    val conclusion: String,
    val callToAction: String,
    val fullFormattedText: String
)

data class VideoPromptResult(
    val title: String,
    val fullPrompt: String,
    val character: String,
    val environment: String,
    val camera: String,
    val lighting: String,
    val motion: String,
    val format: String = "9:16 vertical video (1080x1920)"
)

data class ImagePromptResult(
    val title: String,
    val fullPrompt: String,
    val style: String,
    val composition: String,
    val lighting: String,
    val negativePrompt: String,
    val parameters: String
)

data class AdResult(
    val productName: String,
    val hook: String,
    val script: String,
    val onScreenText: List<String>,
    val callToAction: String,
    val postDescription: String,
    val fullFormattedText: String
)

data class StoryResult(
    val title: String,
    val characters: String,
    val context: String,
    val beginning: String,
    val development: String,
    val climax: String,
    val ending: String,
    val moral: String,
    val fullFormattedText: String
)

data class QuickPackResult(
    val originalIdea: String,
    val hooks: List<String>,
    val script: ScriptResult,
    val videoPrompt: String,
    val imagePrompt: String,
    val scenes: List<String>,
    val hashtags: List<String>,
    val description: String
)
