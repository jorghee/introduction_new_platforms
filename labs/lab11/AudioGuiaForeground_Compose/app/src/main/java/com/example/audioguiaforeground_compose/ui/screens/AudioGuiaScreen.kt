package com.example.audioguiaforeground_compose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.audioguiaforeground_compose.ui.components.BannerCicloVida
import com.example.audioguiaforeground_compose.ui.components.BotonContinuarUltimo
import com.example.audioguiaforeground_compose.ui.components.ControlesPrincipales
import com.example.audioguiaforeground_compose.ui.components.ItemEdificioCampus
import com.example.audioguiaforeground_compose.ui.components.TarjetaEdificioActual
import com.example.audioguiaforeground_compose.ui.viewmodel.AudioGuiaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioGuiaScreen(viewModel: AudioGuiaViewModel) {
    val playbackState by viewModel.playbackState.collectAsState()
    val ultimoEdificioPersistido by viewModel.ultimoEdificioPersistido.collectAsState()

    var enSegundoPlano by remember { mutableStateOf(false) }

    // Observador del ciclo de vida global con ProcessLifecycleOwner (Paso 10 de la guía)
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> enSegundoPlano = true
                Lifecycle.Event.ON_START -> enSegundoPlano = false
                else -> {}
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        onDispose {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Campus Tour",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = "Tour",
                        modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))
                BannerCicloVida(
                    enSegundoPlano = enSegundoPlano,
                    reproduciendo = playbackState.estaReproduciendo
                )
            }

            // Botón "Continuar" último edificio persistido con Room (Ejercicio 3)
            if (ultimoEdificioPersistido != null) {
                item {
                    BotonContinuarUltimo(
                        ultimoEdificio = ultimoEdificioPersistido,
                        onContinuar = {
                            viewModel.continuarUltimoEdificio()
                        }
                    )
                }
            }

            item {
                // Tarjeta principal del edificio en reproducción
                TarjetaEdificioActual(state = playbackState)
            }

            item {
                // Controles de reproducción y avance
                ControlesPrincipales(
                    state = playbackState,
                    onReproducir = { viewModel.reproducir() },
                    onPausar = { viewModel.pausar() },
                    onDetener = { viewModel.detener() },
                    onSiguiente = { viewModel.siguienteEdificio() }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Puntos de Interés",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(viewModel.listaEdificios, key = { it.id }) { edificio ->
                ItemEdificioCampus(
                    edificio = edificio,
                    esActivo = playbackState.edificioActual.id == edificio.id,
                    estaReproduciendo = playbackState.estaReproduciendo && playbackState.edificioActual.id == edificio.id,
                    onSeleccionar = {
                        viewModel.seleccionarEdificio(edificio)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
