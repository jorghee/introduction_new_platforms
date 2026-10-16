package com.example.planointeractivo_compose.viewmodel

import androidx.compose.runtime.Immutable
import com.example.planointeractivo_compose.model.Ambiente
import com.example.planointeractivo_compose.model.Marcador
import com.example.planointeractivo_compose.model.PlanoEdificio

/**
 * Modos de visualización soportados en la aplicación para contrastar
 * el ejercicio resuelto por el docente y los ejercicios propuestos del campus.
 */
enum class ModoVisualizacion {
    PABELLON_EPIS,
    DOCENTE_MUESTRA
}

/**
 * Estado inmutable de la interfaz de usuario (UiState) para el plano interactivo.
 * Sigue el patrón Unidirectional Data Flow (UDF) de Jetpack Compose.
 *
 * @param cargando Indica si la carga de datos desde assets está en progreso.
 * @param plano Datos del plano activo (ambientes, muros y marcadores).
 * @param marcadorSeleccionado Marcador pulsado por el usuario (gatilla el AlertDialog).
 * @param ambienteSeleccionado Ambiente pulsado para resaltado contextual.
 * @param modo Visualización activa (Pabellón EPIS o Muestra del Docente).
 * @param mensajeError Mensaje explicativo en caso de fallas de deserialización.
 */
@Immutable
data class PlanoUiState(
    val cargando: Boolean = false,
    val plano: PlanoEdificio? = null,
    val marcadorSeleccionado: Marcador? = null,
    val ambienteSeleccionado: Ambiente? = null,
    val modo: ModoVisualizacion = ModoVisualizacion.PABELLON_EPIS,
    val mensajeError: String? = null
)
