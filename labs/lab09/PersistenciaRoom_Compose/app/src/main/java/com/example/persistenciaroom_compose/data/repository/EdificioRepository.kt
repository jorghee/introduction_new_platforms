package com.example.persistenciaroom_compose.data.repository

import com.example.persistenciaroom_compose.data.local.dao.EdificioDao
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.entity.NotaEntity
import com.example.persistenciaroom_compose.data.local.relation.AmbienteConNotas
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes

class EdificioRepository(private val dao: EdificioDao) {

    // --- Edificios ---
    suspend fun obtenerTodosLosEdificios(): List<EdificioEntity> = dao.obtenerTodos()
    suspend fun obtenerEdificioPorId(id: Int): EdificioEntity? = dao.obtenerEdificioPorId(id)
    suspend fun insertarEdificio(edificio: EdificioEntity): Long = dao.insertarEdificio(edificio)
    suspend fun actualizarEdificio(edificio: EdificioEntity) = dao.actualizarEdificio(edificio)
    suspend fun eliminarEdificio(edificio: EdificioEntity) = dao.eliminarEdificio(edificio)

    // --- Ambientes ---
    suspend fun obtenerTodosLosAmbientes(): List<AmbienteEntity> = dao.obtenerAmbientes()
    suspend fun obtenerAmbientePorId(id: Int): AmbienteEntity? = dao.obtenerAmbientePorId(id)
    suspend fun obtenerAmbientesPorEdificio(edificioId: Int): List<AmbienteEntity> = dao.obtenerAmbientesPorEdificio(edificioId)
    suspend fun insertarAmbiente(ambiente: AmbienteEntity): Long = dao.insertarAmbiente(ambiente)
    suspend fun actualizarAmbiente(ambiente: AmbienteEntity) = dao.actualizarAmbiente(ambiente)
    suspend fun eliminarAmbiente(ambiente: AmbienteEntity) = dao.eliminarAmbiente(ambiente)

    // --- Notas ---
    suspend fun obtenerTodasLasNotas(): List<NotaEntity> = dao.obtenerNotas()
    suspend fun obtenerNotasPorAmbiente(ambienteId: Int): List<NotaEntity> = dao.obtenerNotasPorAmbiente(ambienteId)
    suspend fun insertarNota(nota: NotaEntity): Long = dao.insertarNota(nota)
    suspend fun actualizarNota(nota: NotaEntity) = dao.actualizarNota(nota)
    suspend fun eliminarNota(nota: NotaEntity) = dao.eliminarNota(nota)

    // --- Consultas Relacionales ---
    suspend fun obtenerEdificiosConAmbientes(): List<EdificioConAmbientes> = dao.obtenerConAmbientes()
    suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas> = dao.obtenerAmbientesConNotas()
}
