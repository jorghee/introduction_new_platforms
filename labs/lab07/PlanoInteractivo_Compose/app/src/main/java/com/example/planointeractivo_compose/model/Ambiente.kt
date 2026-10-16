package com.example.planointeractivo_compose.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

/**
 * Representa un ambiente físico (salón de cómputo, aula, auditorio, patio) dibujado
 * como un rectángulo sobre el Canvas del plano.
 *
 * @param id Identificador único del ambiente.
 * @param nombre Nombre descriptivo del ambiente.
 * @param etiqueta Texto visible dibujado sobre la superficie del ambiente en el Canvas.
 * @param topLeft Coordenada superior izquierda (x, y) del rectángulo.
 * @param size Dimensiones (ancho, alto) del ambiente.
 * @param colorHex Color de relleno del rectángulo en formato hexadecimal.
 * @param bordeHex Color del borde perimétrico del ambiente en formato hexadecimal.
 */
@Immutable
data class Ambiente(
    val id: String,
    val nombre: String,
    val etiqueta: String,
    val topLeft: Offset,
    val size: Size,
    val colorHex: String = "#BBDEFB",
    val bordeHex: String = "#1976D2"
) {
    /**
     * Determina si una coordenada de toque (x, y) se encuentra dentro de los límites
     * rectangulares de este ambiente (Axis-Aligned Bounding Box - AABB).
     */
    fun contienePunto(punto: Offset): Boolean {
        return punto.x >= topLeft.x &&
                punto.x <= (topLeft.x + size.width) &&
                punto.y >= topLeft.y &&
                punto.y <= (topLeft.y + size.height)
    }
}
