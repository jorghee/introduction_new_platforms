package com.example.persistenciaroom_compose.data.repository

import com.example.persistenciaroom_compose.data.local.dao.EdificioDao
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes

class EdificioRepository(private val dao: EdificioDao) {

    suspend fun obtenerTodos(): List<EdificioEntity> = dao.obtenerTodos()

    suspend fun obtenerConAmbientes(): List<EdificioConAmbientes> = dao.obtenerConAmbientes()

    suspend fun insertarEdificio(edificio: EdificioEntity): Long = dao.insertarEdificio(edificio)

    suspend fun insertarAmbiente(ambiente: AmbienteEntity): Long = dao.insertarAmbiente(ambiente)

    suspend fun actualizarEdificio(edificio: EdificioEntity) = dao.actualizarEdificio(edificio)

    suspend fun eliminarEdificio(edificio: EdificioEntity) = dao.eliminarEdificio(edificio)
}
