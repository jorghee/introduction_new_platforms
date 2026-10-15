package com.example.planointeractivo_compose.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

/**
 * Representa un marcador interactivo posicionado sobre el plano del ambiente.
 *
 * @param offset Coordenadas (x, y) del centro del marcador dentro del Canvas.
 * @param radio Radio en píxeles del marcador circular para dibujo y detección de toque.
 * @param titulo Título informativo que se muestra en el diálogo emergente.
 * @param descripcion Descripción detallada del ambiente o recurso señalizado.
 * @param id Identificador único opcional para persistencia o trazabilidad.
 * @param categoria Categoría temática del marcador (Laboratorio, Auditorio, Acceso, etc.).
 * @param colorHex Color en formato hexadecimal para personalizar la apariencia visual.
 */
@Immutable
data class Marcador(
    val offset: Offset,
    val radio: Float,
    val titulo: String,
    val descripcion: String,
    val id: String = "",
    val categoria: String = "General",
    val colorHex: String = "#D32F2F"
)
