package com.example.audioguiaforeground_compose.service

import com.example.audioguiaforeground_compose.data.model.Edificio
import com.example.audioguiaforeground_compose.data.model.EdificiosData

/**
 * Estado inmutable de reproducción expuesto reactivamente a la interfaz de usuario.
 */
data class AudioPlaybackState(
    val servicioActivo: Boolean = false,
    val estaReproduciendo: Boolean = false,
    val estaPausado: Boolean = false,
    val edificioActual: Edificio = EdificiosData.listaEdificios.first(),
    val posicionMs: Int = 0,
    val duracionMs: Int = 0
)
