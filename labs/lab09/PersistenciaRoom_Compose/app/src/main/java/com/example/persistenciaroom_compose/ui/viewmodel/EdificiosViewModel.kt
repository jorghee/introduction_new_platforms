package com.example.persistenciaroom_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes
import com.example.persistenciaroom_compose.data.repository.EdificioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EdificiosViewModel(private val repository: EdificioRepository) : ViewModel() {

    private val _edificios = MutableStateFlow<List<EdificioConAmbientes>>(emptyList())
    val edificios: StateFlow<List<EdificioConAmbientes>> = _edificios.asStateFlow()

    init {
        inicializarDatosYcargar()
    }

    private fun inicializarDatosYcargar() {
        viewModelScope.launch {
            if (repository.obtenerTodos().isEmpty()) {
                val id1 = repository.insertarEdificio(
                    EdificioEntity(nombre = "Biblioteca Central", categoria = "Servicios")
                )
                repository.insertarAmbiente(AmbienteEntity(edificioId = id1.toInt(), nombre = "Sala de lectura"))
                repository.insertarAmbiente(AmbienteEntity(edificioId = id1.toInt(), nombre = "Hemeroteca"))

                val id2 = repository.insertarEdificio(
                    EdificioEntity(nombre = "Facultad de Ingeniería", categoria = "Académico")
                )
                repository.insertarAmbiente(
                    AmbienteEntity(edificioId = id2.toInt(), nombre = "Laboratorio de cómputo")
                )
            }
            recargarEdificios()
        }
    }

    fun recargarEdificios() {
        viewModelScope.launch {
            _edificios.value = repository.obtenerConAmbientes()
        }
    }
}
