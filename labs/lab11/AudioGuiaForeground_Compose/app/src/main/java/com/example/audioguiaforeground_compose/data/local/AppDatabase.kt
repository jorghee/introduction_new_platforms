package com.example.audioguiaforeground_compose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.audioguiaforeground_compose.data.local.dao.UltimoEdificioDao
import com.example.audioguiaforeground_compose.data.local.entity.UltimoEdificioEntity

/**
 * Base de datos Room para la persistencia del estado de reproducción de la audio-guía.
 */
@Database(
    entities = [UltimoEdificioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ultimoEdificioDao(): UltimoEdificioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "audioguia_campus.db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
