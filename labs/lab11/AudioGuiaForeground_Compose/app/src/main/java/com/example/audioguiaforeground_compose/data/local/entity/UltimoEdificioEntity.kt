package com.example.audioguiaforeground_compose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para persistir el último edificio reproducido en la audio-guía (Ejercicio Propuesto 3).
 */
@Entity(tableName = "ultimo_edificio")
data class UltimoEdificioEntity(
    @PrimaryKey
    val id: Int = 1,
    val edificioId: Int,
    val nombreEdificio: String,
    val categoria: String,
    val timestamp: Long = System.currentTimeMillis()
)
