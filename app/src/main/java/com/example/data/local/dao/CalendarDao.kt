package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    @Query("SELECT * FROM calendar_entries ORDER BY dateIso ASC, timeStr ASC")
    fun getAllEntries(): Flow<List<CalendarEntity>>

    @Query("SELECT * FROM calendar_entries WHERE dateIso = :dateIso ORDER BY timeStr ASC")
    fun getEntriesForDate(dateIso: String): Flow<List<CalendarEntity>>

    @Query("SELECT COUNT(*) FROM calendar_entries")
    fun getEntryCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: CalendarEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<CalendarEntity>)

    @Update
    suspend fun updateEntry(entry: CalendarEntity)

    @Delete
    suspend fun deleteEntry(entry: CalendarEntity)

    @Query("DELETE FROM calendar_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)

    @Query("DELETE FROM calendar_entries")
    suspend fun deleteAll()
}
