package com.example.audioguiaforeground_compose.data.repository

import com.example.audioguiaforeground_compose.data.local.dao.UltimoEdificioDao
import com.example.audioguiaforeground_compose.data.local.entity.UltimoEdificioEntity
import com.example.audioguiaforeground_compose.data.model.Edificio
import com.example.audioguiaforeground_compose.data.model.EdificiosData
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que centraliza el acceso a la lista de edificios y la persistencia de Room.
 */
class AudioGuiaRepository(private val dao: UltimoEdificioDao) {

    fun obtenerTodosLosEdificios(): List<Edificio> = EdificiosData.listaEdificios

    fun obtenerEdificioPorId(id: Int): Edificio = EdificiosData.obtenerPorId(id)

    fun obtenerSiguienteEdificio(idActual: Int): Edificio = EdificiosData.obtenerSiguiente(idActual)

    fun observarUltimoEdificio(): Flow<UltimoEdificioEntity?> = dao.observarUltimoEdificio()

    suspend fun obtenerUltimoEdificio(): UltimoEdificioEntity? = dao.obtenerUltimoEdificio()

    suspend fun guardarUltimoEdificio(edificio: Edificio) {
        val entidad = UltimoEdificioEntity(
            id = 1,
            edificioId = edificio.id,
            nombreEdificio = edificio.nombre,
            categoria = edificio.categoria,
            timestamp = System.currentTimeMillis()
        )
        dao.guardarUltimoEdificio(entidad)
    }
}
