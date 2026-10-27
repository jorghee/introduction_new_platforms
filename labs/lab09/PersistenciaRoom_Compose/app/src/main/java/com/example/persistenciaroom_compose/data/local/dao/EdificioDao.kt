package com.example.persistenciaroom_compose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes

@Dao
interface EdificioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEdificio(edificio: EdificioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAmbiente(ambiente: AmbienteEntity): Long

    @Update
    suspend fun actualizarEdificio(edificio: EdificioEntity)

    @Delete
    suspend fun eliminarEdificio(edificio: EdificioEntity)

    @Query("SELECT * FROM EdificioEntity")
    suspend fun obtenerTodos(): List<EdificioEntity>

    @Transaction
    @Query("SELECT * FROM EdificioEntity")
    suspend fun obtenerConAmbientes(): List<EdificioConAmbientes>
}
