package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.CalendarEntry
import com.example.domain.model.ContentStatus
import com.example.domain.model.ContentType
import com.example.domain.model.CreatorProject
import com.example.domain.model.Platform

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: String, // from ContentType.name
    val dateEpoch: Long,
    val preview: String,
    val content: String,
    val platform: String, // from Platform.name
    val country: String,
    val isFavorite: Boolean = false,
    val tags: String = ""
) {
    fun toDomain(): CreatorProject {
        val safeType = runCatching { ContentType.valueOf(type) }.getOrDefault(ContentType.SCRIPT)
        val safePlatform = runCatching { Platform.valueOf(platform) }.getOrDefault(Platform.TIKTOK)
        return CreatorProject(
            id = id,
            title = title,
            type = safeType,
            dateEpoch = dateEpoch,
            preview = preview,
            content = content,
            platform = safePlatform,
            country = country,
            isFavorite = isFavorite,
            tags = tags
        )
    }

    companion object {
        fun fromDomain(project: CreatorProject): ProjectEntity {
            return ProjectEntity(
                id = project.id,
                title = project.title,
                type = project.type.name,
                dateEpoch = project.dateEpoch,
                preview = project.preview,
                content = project.content,
                platform = project.platform.name,
                country = project.country,
                isFavorite = project.isFavorite,
                tags = project.tags
            )
        }
    }
}

@Entity(tableName = "calendar_entries")
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateIso: String,
    val timeStr: String,
    val title: String,
    val platform: String,
    val contentType: String,
    val status: String,
    val notes: String = "",
    val linkedProjectId: Long? = null
) {
    fun toDomain(): CalendarEntry {
        val safePlatform = runCatching { Platform.valueOf(platform) }.getOrDefault(Platform.TIKTOK)
        val safeType = runCatching { ContentType.valueOf(contentType) }.getOrDefault(ContentType.VIDEO_IDEA)
        val safeStatus = runCatching { ContentStatus.valueOf(status) }.getOrDefault(ContentStatus.IDEA)
        return CalendarEntry(
            id = id,
            dateIso = dateIso,
            timeStr = timeStr,
            title = title,
            platform = safePlatform,
            contentType = safeType,
            status = safeStatus,
            notes = notes,
            linkedProjectId = linkedProjectId
        )
    }

    companion object {
        fun fromDomain(entry: CalendarEntry): CalendarEntity {
            return CalendarEntity(
                id = entry.id,
                dateIso = entry.dateIso,
                timeStr = entry.timeStr,
                title = entry.title,
                platform = entry.platform.name,
                contentType = entry.contentType.name,
                status = entry.status.name,
                notes = entry.notes,
                linkedProjectId = entry.linkedProjectId
            )
        }
    }
}
