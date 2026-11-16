package com.example.audioguiaforeground_compose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.audioguiaforeground_compose.data.local.entity.UltimoEdificioEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para las operaciones sobre el último edificio reproducido.
 */
@Dao
interface UltimoEdificioDao {

    @Query("SELECT * FROM ultimo_edificio WHERE id = 1")
    fun observarUltimoEdificio(): Flow<UltimoEdificioEntity?>

    @Query("SELECT * FROM ultimo_edificio WHERE id = 1")
    suspend fun obtenerUltimoEdificio(): UltimoEdificioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUltimoEdificio(entidad: UltimoEdificioEntity)

    @Query("DELETE FROM ultimo_edificio")
    suspend fun limpiarUltimoEdificio()
}
