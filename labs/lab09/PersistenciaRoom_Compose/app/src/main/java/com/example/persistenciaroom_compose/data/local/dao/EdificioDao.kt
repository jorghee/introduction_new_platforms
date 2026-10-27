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
import com.example.persistenciaroom_compose.data.local.entity.NotaEntity
import com.example.persistenciaroom_compose.data.local.relation.AmbienteConNotas
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes

@Dao
interface EdificioDao {

    // ==========================================
    // Operaciones para EdificioEntity (Tabla 1)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEdificio(edificio: EdificioEntity): Long

    @Update
    suspend fun actualizarEdificio(edificio: EdificioEntity)

    @Delete
    suspend fun eliminarEdificio(edificio: EdificioEntity)

    @Query("SELECT * FROM EdificioEntity ORDER BY id ASC")
    suspend fun obtenerTodos(): List<EdificioEntity>

    @Query("SELECT * FROM EdificioEntity WHERE id = :id")
    suspend fun obtenerEdificioPorId(id: Int): EdificioEntity?

    // ==========================================
    // Operaciones para AmbienteEntity (Tabla 2)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAmbiente(ambiente: AmbienteEntity): Long

    @Update
    suspend fun actualizarAmbiente(ambiente: AmbienteEntity)

    @Delete
    suspend fun eliminarAmbiente(ambiente: AmbienteEntity)

    @Query("SELECT * FROM AmbienteEntity ORDER BY id ASC")
    suspend fun obtenerAmbientes(): List<AmbienteEntity>

    @Query("SELECT * FROM AmbienteEntity WHERE id = :id")
    suspend fun obtenerAmbientePorId(id: Int): AmbienteEntity?

    @Query("SELECT * FROM AmbienteEntity WHERE edificioId = :edificioId")
    suspend fun obtenerAmbientesPorEdificio(edificioId: Int): List<AmbienteEntity>

    // ==========================================
    // Operaciones para NotaEntity (Tabla 3)
    // ==========================================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: NotaEntity): Long

    @Update
    suspend fun actualizarNota(nota: NotaEntity)

    @Delete
    suspend fun eliminarNota(nota: NotaEntity)

    @Query("SELECT * FROM NotaEntity ORDER BY id ASC")
    suspend fun obtenerNotas(): List<NotaEntity>

    @Query("SELECT * FROM NotaEntity WHERE ambienteId = :ambienteId")
    suspend fun obtenerNotasPorAmbiente(ambienteId: Int): List<NotaEntity>

    // ==========================================
    // Consultas Relacionales (1:N)
    // ==========================================
    // Consulta Relacional 1: Edificio con sus Ambientes asociados
    @Transaction
    @Query("SELECT * FROM EdificioEntity ORDER BY id ASC")
    suspend fun obtenerConAmbientes(): List<EdificioConAmbientes>

    // Consulta Relacional 2: Ambiente con sus Notas asociadas
    @Transaction
    @Query("SELECT * FROM AmbienteEntity ORDER BY id ASC")
    suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas>
}
