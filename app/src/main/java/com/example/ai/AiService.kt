package com.example.ai

import com.example.domain.model.Platform

interface AiService {
    val isDemoMode: Boolean

    suspend fun generateIdeas(
        topic: String,
        platform: Platform,
        theme: String,
        duration: String,
        style: String,
        country: String,
        city: String,
        language: String
    ): List<String>

    suspend fun generateHooks(
        topic: String,
        style: String,
        platform: Platform,
        country: String,
        language: String
    ): List<String>

    suspend fun generateScript(
        topic: String,
        duration: String,
        platform: Platform,
        tone: String,
        country: String,
        language: String
    ): ScriptResult

    suspend fun generateVideoPrompt(
        character: String,
        appearance: String,
        environment: String,
        action: String,
        camera: String,
        lighting: String,
        movement: String,
        style: String
    ): VideoPromptResult

    suspend fun generateImagePrompt(
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
    ): ImagePromptResult

    suspend fun generateAdvertisement(
        productName: String,
        price: String,
        description: String,
        targetAudience: String,
        contact: String,
        location: String
    ): AdResult

    suspend fun generateStory(
        idea: String,
        country: String
    ): StoryResult

    suspend fun generateHashtags(
        topic: String,
        platform: Platform,
        country: String
    ): List<String>

    suspend fun generateQuickPack(
        idea: String,
        country: String,
        platform: Platform
    ): QuickPackResult
}
