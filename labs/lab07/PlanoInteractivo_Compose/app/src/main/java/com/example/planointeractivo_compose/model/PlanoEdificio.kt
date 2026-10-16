package com.example.planointeractivo_compose.model

import androidx.compose.runtime.Immutable

/**
 * Contenedor de datos completo que representa el plano interactivo de un edificio o pabellón.
 *
 * @param edificio Nombre oficial del edificio o facultad.
 * @param piso Nivel o piso representado.
 * @param descripcion Descripción contextual del ambiente o pabellón.
 * @param ambientes Lista de salas, aulas o patios representados con rectángulos.
 * @param lineas Muros divisorios y trazados arquitectónicos.
 * @param marcadores Marcadores de interés interactivos distribuidos sobre el plano.
 */
@Immutable
data class PlanoEdificio(
    val edificio: String,
    val piso: String,
    val descripcion: String,
    val ambientes: List<Ambiente>,
    val lineas: List<LineaPlano>,
    val marcadores: List<Marcador>
)
