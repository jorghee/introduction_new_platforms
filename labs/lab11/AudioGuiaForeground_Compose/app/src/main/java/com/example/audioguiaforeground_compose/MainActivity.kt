package com.example.audioguiaforeground_compose

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.audioguiaforeground_compose.data.local.AppDatabase
import com.example.audioguiaforeground_compose.data.repository.AudioGuiaRepository
import com.example.audioguiaforeground_compose.ui.screens.AudioGuiaScreen
import com.example.audioguiaforeground_compose.ui.theme.AudioGuiaForeground_ComposeTheme
import com.example.audioguiaforeground_compose.ui.viewmodel.AudioGuiaViewModel
import com.example.audioguiaforeground_compose.ui.viewmodel.AudioGuiaViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = AudioGuiaRepository(database.ultimoEdificioDao())
        val factory = AudioGuiaViewModelFactory(application, repository)

        setContent {
            AudioGuiaForeground_ComposeTheme {
                // Solicitar permiso de notificaciones en Android 13+ (Tiramisu / API 33+)
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { /* Manejo de respuesta del usuario */ }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val yaOtorgado = ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                        if (!yaOtorgado) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                val viewModel: AudioGuiaViewModel = viewModel(factory = factory)

                Surface(modifier = Modifier.fillMaxSize()) {
                    AudioGuiaScreen(viewModel = viewModel)
                }
            }
        }
    }
}
