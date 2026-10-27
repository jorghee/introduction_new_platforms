package com.example.persistenciaroom_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.persistenciaroom_compose.data.repository.EdificioRepository

class EdificiosViewModelFactory(
    private val repository: EdificioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EdificiosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EdificiosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
