package com.example.planointeractivo_compose.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

/**
 * Representa una línea divisoria o muro dibujado en el Canvas del plano.
 *
 * @param start Punto de inicio de la línea (x, y).
 * @param end Punto final de la línea (x, y).
 * @param colorHex Color del trazo en formato hexadecimal.
 * @param grosor Grosor en píxeles del trazo (strokeWidth).
 */
@Immutable
data class LineaPlano(
    val start: Offset,
    val end: Offset,
    val colorHex: String = "#424242",
    val grosor: Float = 4f
)
