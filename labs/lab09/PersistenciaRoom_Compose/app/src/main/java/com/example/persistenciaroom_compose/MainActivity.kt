package com.example.persistenciaroom_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.persistenciaroom_compose.data.local.AppDatabase
import com.example.persistenciaroom_compose.data.repository.EdificioRepository
import com.example.persistenciaroom_compose.ui.screens.EdificiosScreen
import com.example.persistenciaroom_compose.ui.theme.PersistenciaRoom_ComposeTheme
import com.example.persistenciaroom_compose.ui.viewmodel.EdificiosViewModel
import com.example.persistenciaroom_compose.ui.viewmodel.EdificiosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = EdificioRepository(database.edificioDao())
        val factory = EdificiosViewModelFactory(repository)

        setContent {
            PersistenciaRoom_ComposeTheme {
                val viewModel: EdificiosViewModel = viewModel(factory = factory)
                EdificiosScreen(viewModel = viewModel)
            }
        }
    }
}