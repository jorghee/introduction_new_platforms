package com.example.persistenciaroom_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.entity.NotaEntity
import com.example.persistenciaroom_compose.data.local.relation.AmbienteConNotas
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes
import com.example.persistenciaroom_compose.data.repository.EdificioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EdificiosViewModel(private val repository: EdificioRepository) : ViewModel() {

    private val _edificiosConAmbientes = MutableStateFlow<List<EdificioConAmbientes>>(emptyList())
    val edificiosConAmbientes: StateFlow<List<EdificioConAmbientes>> = _edificiosConAmbientes.asStateFlow()

    private val _ambientesConNotas = MutableStateFlow<List<AmbienteConNotas>>(emptyList())
    val ambientesConNotas: StateFlow<List<AmbienteConNotas>> = _ambientesConNotas.asStateFlow()

    init {
        inicializarBaseDeDatos()
    }

    private fun inicializarBaseDeDatos() {
        viewModelScope.launch {
            if (repository.obtenerTodosLosEdificios().isEmpty()) {
                // Edificio 1: Pabellón EPIS
                val idEpis = repository.insertarEdificio(
                    EdificioEntity(nombre = "Pabellón EPIS", categoria = "Académico")
                ).toInt()
                val idLabRedes = repository.insertarAmbiente(
                    AmbienteEntity(edificioId = idEpis, nombre = "Lab 101 - Redes y Conectividad")
                ).toInt()
                repository.insertarNota(
                    NotaEntity(ambienteId = idLabRedes, texto = "Racks y switches de laboratorio operativos")
                )
                repository.insertarNota(
                    NotaEntity(ambienteId = idLabRedes, texto = "Capacidad física: 32 estaciones de trabajo")
                )

                val idLabSoft = repository.insertarAmbiente(
                    AmbienteEntity(edificioId = idEpis, nombre = "Lab 102 - Desarrollo de Software")
                ).toInt()
                repository.insertarNota(
                    NotaEntity(ambienteId = idLabSoft, texto = "Equipos configurados con Android Studio y Compose")
                )

                // Edificio 2: Biblioteca Central
                val idBiblio = repository.insertarEdificio(
                    EdificioEntity(nombre = "Biblioteca Central", categoria = "Servicios")
                ).toInt()
                val idSalaLectura = repository.insertarAmbiente(
                    AmbienteEntity(edificioId = idBiblio, nombre = "Sala de Lectura Principal")
                ).toInt()
                repository.insertarNota(
                    NotaEntity(ambienteId = idSalaLectura, texto = "Horario ininterrumpido de 07:00 a 20:00")
                )
                val idHemeroteca = repository.insertarAmbiente(
                    AmbienteEntity(edificioId = idBiblio, nombre = "Hemeroteca y Tesis")
                ).toInt()
                repository.insertarNota(
                    NotaEntity(ambienteId = idHemeroteca, texto = "Consulta de material bibliográfico de posgrado")
                )

                // Edificio 3: Auditorio Magno
                val idAuditorio = repository.insertarEdificio(
                    EdificioEntity(nombre = "Auditorio Magno", categoria = "Institucional")
                ).toInt()
                val idSalaMagna = repository.insertarAmbiente(
                    AmbienteEntity(edificioId = idAuditorio, nombre = "Sala Principal de Grados")
                ).toInt()
                repository.insertarNota(
                    NotaEntity(ambienteId = idSalaMagna, texto = "Proyector láser y microfonía inalámbrica verificados")
                )
            }
            recargarTodo()
        }
    }

    fun recargarTodo() {
        viewModelScope.launch {
            _edificiosConAmbientes.value = repository.obtenerEdificiosConAmbientes()
            _ambientesConNotas.value = repository.obtenerAmbientesConNotas()
        }
    }

    // ==========================================
    // Acciones CRUD: Edificios
    // ==========================================
    fun agregarEdificio(nombre: String, categoria: String) {
        viewModelScope.launch {
            if (nombre.isNotBlank() && categoria.isNotBlank()) {
                repository.insertarEdificio(EdificioEntity(nombre = nombre.trim(), categoria = categoria.trim()))
                recargarTodo()
            }
        }
    }

    fun actualizarEdificio(id: Int, nuevoNombre: String, nuevaCategoria: String) {
        viewModelScope.launch {
            if (nuevoNombre.isNotBlank() && nuevaCategoria.isNotBlank()) {
                repository.actualizarEdificio(EdificioEntity(id = id, nombre = nuevoNombre.trim(), categoria = nuevaCategoria.trim()))
                recargarTodo()
            }
        }
    }

    fun eliminarEdificio(edificio: EdificioEntity) {
        viewModelScope.launch {
            repository.eliminarEdificio(edificio)
            recargarTodo()
        }
    }

    // ==========================================
    // Acciones CRUD: Ambientes
    // ==========================================
    fun agregarAmbiente(edificioId: Int, nombre: String) {
        viewModelScope.launch {
            if (nombre.isNotBlank()) {
                repository.insertarAmbiente(AmbienteEntity(edificioId = edificioId, nombre = nombre.trim()))
                recargarTodo()
            }
        }
    }

    fun actualizarAmbiente(id: Int, edificioId: Int, nuevoNombre: String) {
        viewModelScope.launch {
            if (nuevoNombre.isNotBlank()) {
                repository.actualizarAmbiente(AmbienteEntity(id = id, edificioId = edificioId, nombre = nuevoNombre.trim()))
                recargarTodo()
            }
        }
    }

    fun eliminarAmbiente(ambiente: AmbienteEntity) {
        viewModelScope.launch {
            repository.eliminarAmbiente(ambiente)
            recargarTodo()
        }
    }

    // ==========================================
    // Acciones CRUD: Notas
    // ==========================================
    fun agregarNota(ambienteId: Int, texto: String) {
        viewModelScope.launch {
            if (texto.isNotBlank()) {
                repository.insertarNota(NotaEntity(ambienteId = ambienteId, texto = texto.trim()))
                recargarTodo()
            }
        }
    }

    fun actualizarNota(id: Int, ambienteId: Int, nuevoTexto: String) {
        viewModelScope.launch {
            if (nuevoTexto.isNotBlank()) {
                repository.actualizarNota(NotaEntity(id = id, ambienteId = ambienteId, texto = nuevoTexto.trim()))
                recargarTodo()
            }
        }
    }

    fun eliminarNota(nota: NotaEntity) {
        viewModelScope.launch {
            repository.eliminarNota(nota)
            recargarTodo()
        }
    }
}
