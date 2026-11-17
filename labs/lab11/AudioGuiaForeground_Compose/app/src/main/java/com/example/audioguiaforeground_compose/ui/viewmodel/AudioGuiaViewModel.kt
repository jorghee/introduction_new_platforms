package com.example.audioguiaforeground_compose.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audioguiaforeground_compose.data.local.entity.UltimoEdificioEntity
import com.example.audioguiaforeground_compose.data.model.Edificio
import com.example.audioguiaforeground_compose.data.model.EdificiosData
import com.example.audioguiaforeground_compose.data.repository.AudioGuiaRepository
import com.example.audioguiaforeground_compose.service.AudioGuiaService
import com.example.audioguiaforeground_compose.service.AudioPlaybackState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel que expone el estado reactivo del Foreground Service y de la base de datos Room.
 */
class AudioGuiaViewModel(
    application: Application,
    private val repository: AudioGuiaRepository
) : AndroidViewModel(application) {

    val listaEdificios: List<Edificio> = repository.obtenerTodosLosEdificios()

    // Estado reactivo sincronizado con el Foreground Service
    val playbackState: StateFlow<AudioPlaybackState> = AudioGuiaService.playbackState

    // Estado del último edificio reproducido persistido en Room (Ejercicio 3)
    val ultimoEdificioPersistido: StateFlow<UltimoEdificioEntity?> =
        repository.observarUltimoEdificio()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    fun reproducir(edificioId: Int? = null) {
        val id = edificioId ?: playbackState.value.edificioActual.id
        val intent = Intent(getApplication(), AudioGuiaService::class.java).apply {
            action = AudioGuiaService.ACCION_REPRODUCIR
            putExtra(AudioGuiaService.EXTRA_EDIFICIO_ID, id)
        }
        iniciarServicio(intent)
    }

    fun pausar() {
        val intent = Intent(getApplication(), AudioGuiaService::class.java).apply {
            action = AudioGuiaService.ACCION_PAUSAR
        }
        iniciarServicio(intent)
    }

    fun detener() {
        val intent = Intent(getApplication(), AudioGuiaService::class.java).apply {
            action = AudioGuiaService.ACCION_DETENER
        }
        iniciarServicio(intent)
    }

    fun siguienteEdificio() {
        val intent = Intent(getApplication(), AudioGuiaService::class.java).apply {
            action = AudioGuiaService.ACCION_SIGUIENTE
        }
        iniciarServicio(intent)
    }

    fun seleccionarEdificio(edificio: Edificio) {
        val intent = Intent(getApplication(), AudioGuiaService::class.java).apply {
            action = AudioGuiaService.ACCION_SELECCIONAR
            putExtra(AudioGuiaService.EXTRA_EDIFICIO_ID, edificio.id)
        }
        iniciarServicio(intent)
    }

    fun continuarUltimoEdificio() {
        val ultimo = ultimoEdificioPersistido.value ?: return
        reproducir(ultimo.edificioId)
    }

    private fun iniciarServicio(intent: Intent) {
        val context = getApplication<Application>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
