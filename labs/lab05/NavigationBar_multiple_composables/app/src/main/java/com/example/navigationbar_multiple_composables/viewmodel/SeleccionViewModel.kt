package com.example.navigationbar_multiple_composables.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SeleccionViewModel : ViewModel() {
    private val _edificioSeleccionado = MutableStateFlow("Ninguno")
    val edificioSeleccionado: StateFlow<String> = _edificioSeleccionado.asStateFlow()

    fun seleccionarEdificio(nombre: String) {
        _edificioSeleccionado.value = nombre
    }
}
