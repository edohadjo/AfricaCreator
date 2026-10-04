package com.example.domain.model

enum class ContentType(val label: String, val icon: String) {
    VIDEO_IDEA("Idée de vidéo", "🎬"),
    HOOK("Hook / Accroche", "🔥"),
    SCRIPT("Script complet", "✍️"),
    VIDEO_PROMPT("Prompt vidéo", "🎥"),
    IMAGE_PROMPT("Prompt image", "🖼️"),
    AD_MAKER("Publicité", "📢"),
    STORY("Histoire / Story", "📖"),
    HASHTAGS("Hashtags", "#️⃣"),
    QUICK_PACK("Pack Créateur", "✨")
}

enum class Platform(val displayName: String, val icon: String) {
    TIKTOK("TikTok", "📱"),
    YOUTUBE("YouTube", "▶️"),
    INSTAGRAM("Instagram", "📸"),
    FACEBOOK("Facebook", "👥")
}

enum class ContentStatus(val label: String, val colorHex: Long) {
    IDEA("Idée", 0xFF8A90A2),
    TO_PREPARE("À préparer", 0xFFE5A93C),
    READY("Prêt", 0xFF2E7D5A),
    PUBLISHED("Publié", 0xFF1976D2)
}
