package com.example.audioguiaforeground_compose.data.model

import androidx.annotation.RawRes
import com.example.audioguiaforeground_compose.R

/**
 * Modelo de dominio que representa una edificación del campus universitario.
 */
data class Edificio(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    @param:RawRes val audioResId: Int = R.raw.audioguia_epis
)

object EdificiosData {
    val listaEdificios = listOf(
        Edificio(
            id = 1,
            nombre = "Pabellón EPIS",
            categoria = "Académico - Ingeniería",
            descripcion = "Escuela Profesional de Ingeniería de Sistemas. Alberga laboratorios de conectividad, servidores y aulas de formación tecnológica avanzada.",
            audioResId = R.raw.audioguia_epis
        ),
        Edificio(
            id = 2,
            nombre = "Biblioteca Central",
            categoria = "Servicios Académicos",
            descripcion = "Principal centro bibliográfico y de investigación documental del campus universitario, dotado de salas de lectura y hemeroteca.",
            audioResId = R.raw.audioguia_epis
        ),
        Edificio(
            id = 3,
            nombre = "Auditorio Magno",
            categoria = "Institucional y Grados",
            descripcion = "Recinto oficial para ceremonias protocolares, defensas de tesis, congresos académicos y conferencias magistrales internacionales.",
            audioResId = R.raw.audioguia_epis
        ),
        Edificio(
            id = 4,
            nombre = "Laboratorio de IA y Cómputo",
            categoria = "Investigación y Desarrollo",
            descripcion = "Centro de cómputo de alto rendimiento especializado en entrenamiento de modelos de inteligencia artificial y computación gráfica.",
            audioResId = R.raw.audioguia_epis
        ),
        Edificio(
            id = 5,
            nombre = "Pabellón de Ingenierías",
            categoria = "Académico y Administrativo",
            descripcion = "Edificio multi-facultativo de ingenierías que centraliza decanatos, salas de docentes y aulas multidisciplinarias del campus.",
            audioResId = R.raw.audioguia_epis
        )
    )

    fun obtenerPorId(id: Int): Edificio {
        return listaEdificios.find { it.id == id } ?: listaEdificios.first()
    }

    fun obtenerSiguiente(idActual: Int): Edificio {
        val index = listaEdificios.indexOfFirst { it.id == idActual }
        return if (index == -1 || index >= listaEdificios.lastIndex) {
            listaEdificios.first()
        } else {
            listaEdificios[index + 1]
        }
    }
}
