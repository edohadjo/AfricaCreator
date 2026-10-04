package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.ProjectDao
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.ProjectEntity

@Database(
    entities = [ProjectEntity::class, CalendarEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AfricaCreatorDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun calendarDao(): CalendarDao

    companion object {
        @Volatile
        private var INSTANCE: AfricaCreatorDatabase? = null

        fun getInstance(context: Context): AfricaCreatorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AfricaCreatorDatabase::class.java,
                    "africacreator_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
