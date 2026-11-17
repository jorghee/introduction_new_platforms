package com.example.audioguiaforeground_compose.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.audioguiaforeground_compose.data.repository.AudioGuiaRepository

class AudioGuiaViewModelFactory(
    private val application: Application,
    private val repository: AudioGuiaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioGuiaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AudioGuiaViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}
