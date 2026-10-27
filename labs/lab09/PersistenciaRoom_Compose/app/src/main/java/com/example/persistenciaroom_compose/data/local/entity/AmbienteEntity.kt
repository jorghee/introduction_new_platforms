package com.example.persistenciaroom_compose.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "AmbienteEntity",
    foreignKeys = [
        ForeignKey(
            entity = EdificioEntity::class,
            parentColumns = ["id"],
            childColumns = ["edificioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["edificioId"])]
)
data class AmbienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val edificioId: Int,
    val nombre: String
)
