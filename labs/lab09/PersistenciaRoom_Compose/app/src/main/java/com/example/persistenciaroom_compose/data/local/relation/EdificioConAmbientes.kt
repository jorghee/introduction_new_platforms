package com.example.persistenciaroom_compose.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity

data class EdificioConAmbientes(
    @Embedded val edificio: EdificioEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "edificioId"
    )
    val ambientes: List<AmbienteEntity>
)
