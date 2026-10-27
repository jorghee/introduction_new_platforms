package com.example.persistenciaroom_compose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.persistenciaroom_compose.data.local.dao.EdificioDao
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity

@Database(
    entities = [EdificioEntity::class, AmbienteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun edificioDao(): EdificioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
