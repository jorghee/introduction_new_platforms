package com.example.planointeractivo_compose.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planointeractivo_compose.data.PlanoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que centraliza y gestiona el estado reactivo del plano interactivo.
 *
 * Cumple con el Ejercicio Propuesto 3 al desacoplar el estado y la lógica de detección
 * de toques fuera del Composable, exponiendo un flujo StateFlow inmutable.
 */
class PlanoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlanoRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(PlanoUiState(cargando = true))
    val uiState: StateFlow<PlanoUiState> = _uiState.asStateFlow()

    init {
        cargarPlanoEpis()
    }

    /**
     * Carga el plano completo de 4+ ambientes del pabellón EPIS desde assets/plano_epis.json.
     */
    fun cargarPlanoEpis() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, mensajeError = null) }
            val resultado = repository.cargarPlanoDesdeAssets("plano_epis.json")
            resultado.fold(
                onSuccess = { planoCargado ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            plano = planoCargado,
                            modo = ModoVisualizacion.PABELLON_EPIS,
                            marcadorSeleccionado = null,
                            ambienteSeleccionado = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            mensajeError = "Error al leer plano_epis.json: ${error.localizedMessage}"
                        )
                    }
                }
            )
        }
    }

    /**
     * Carga el plano demostrativo del docente (2 salones, 2 marcadores).
     */
    fun cargarPlanoDocente() {
        val planoMuestra = repository.obtenerPlanoDocenteMuestra()
        _uiState.update {
            it.copy(
                cargando = false,
                plano = planoMuestra,
                modo = ModoVisualizacion.DOCENTE_MUESTRA,
                marcadorSeleccionado = null,
                ambienteSeleccionado = null,
                mensajeError = null
            )
        }
    }

    /**
     * Procesa una coordenada de toque (Offset) sobre el Canvas.
     *
     * 1. Evalúa si el punto colisiona con algún marcador circular:
     *    distancia euclidiana: ||offset - centro|| <= radio
     * 2. Si no tocó ningún marcador, evalúa si tocó la superficie de algún ambiente (AABB).
     * 3. Si no tocó nada relevante, limpia la selección.
     */
    fun procesarToqueEnCanvas(offset: Offset) {
        val planoActual = _uiState.value.plano ?: return

        // 1. Detección sobre marcadores circulares (mayor prioridad de interacción)
        val marcadorTocado = planoActual.marcadores.firstOrNull { marcador ->
            (offset - marcador.offset).getDistance() <= marcador.radio
        }

        if (marcadorTocado != null) {
            _uiState.update {
                it.copy(
                    marcadorSeleccionado = marcadorTocado,
                    ambienteSeleccionado = null
                )
            }
            return
        }

        // 2. Detección sobre ambientes rectangulares (Bounding Box)
        val ambienteTocado = planoActual.ambientes.firstOrNull { ambiente ->
            ambiente.contienePunto(offset)
        }

        _uiState.update {
            it.copy(
                marcadorSeleccionado = null,
                ambienteSeleccionado = ambienteTocado
            )
        }
    }

    /**
     * Cierra el diálogo emergente del marcador seleccionado.
     */
    fun cerrarDialogoMarcador() {
        _uiState.update { it.copy(marcadorSeleccionado = null) }
    }

    /**
     * Limpia cualquier ambiente o marcador seleccionado.
     */
    fun limpiarSeleccion() {
        _uiState.update {
            it.copy(
                marcadorSeleccionado = null,
                ambienteSeleccionado = null
            )
        }
    }

    /**
     * Permite alternar rápidamente entre la visualización del pabellón EPIS (4 ambientes)
     * y la muestra docente inicial.
     */
    fun alternarModo(nuevoModo: ModoVisualizacion) {
        when (nuevoModo) {
            ModoVisualizacion.PABELLON_EPIS -> cargarPlanoEpis()
            ModoVisualizacion.DOCENTE_MUESTRA -> cargarPlanoDocente()
        }
    }
}
