package com.example.persistenciaroom_compose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.entity.NotaEntity
import com.example.persistenciaroom_compose.ui.components.AmbienteNotaItem
import com.example.persistenciaroom_compose.ui.components.DialogoAmbiente
import com.example.persistenciaroom_compose.ui.components.DialogoEdificio
import com.example.persistenciaroom_compose.ui.components.DialogoNota
import com.example.persistenciaroom_compose.ui.components.EdificioItem
import com.example.persistenciaroom_compose.ui.viewmodel.EdificiosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdificiosScreen(
    viewModel: EdificiosViewModel,
    modifier: Modifier = Modifier
) {
    val edificiosConAmbientes by viewModel.edificiosConAmbientes.collectAsState()
    val ambientesConNotas by viewModel.ambientesConNotas.collectAsState()

    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    // Estados para controlar los diálogos modales CRUD
    var mostrarCrearEdificio by remember { mutableStateOf(false) }
    var edificioAEditar by remember { mutableStateOf<EdificioEntity?>(null) }

    var edificioIdParaNuevoAmbiente by remember { mutableStateOf<Int?>(null) }
    var ambienteAEditar by remember { mutableStateOf<AmbienteEntity?>(null) }

    var ambienteIdParaNuevaNota by remember { mutableStateOf<Int?>(null) }
    var notaAEditar by remember { mutableStateOf<NotaEntity?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Persistencia Room - Campus UNSA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (pestanaSeleccionada == 0) {
                ExtendedFloatingActionButton(
                    onClick = { mostrarCrearEdificio = true }
                ) {
                    Text("+ Nuevo Edificio", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Pestañas para alternar entre las dos consultas relacionales requeridas
            TabRow(selectedTabIndex = pestanaSeleccionada) {
                Tab(
                    selected = pestanaSeleccionada == 0,
                    onClick = { pestanaSeleccionada = 0 },
                    text = {
                        Text(
                            text = "1. Edificios y Ambientes",
                            fontWeight = if (pestanaSeleccionada == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = pestanaSeleccionada == 1,
                    onClick = { pestanaSeleccionada = 1 },
                    text = {
                        Text(
                            text = "2. Ambientes y Notas",
                            fontWeight = if (pestanaSeleccionada == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (pestanaSeleccionada) {
                    0 -> {
                        // Vista Consulta Relacional 1: EdificioConAmbientes
                        if (edificiosConAmbientes.isEmpty()) {
                            Text(
                                text = "No hay edificios registrados en la base de datos.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(edificiosConAmbientes, key = { it.edificio.id }) { item ->
                                    EdificioItem(
                                        item = item,
                                        onEditarEdificio = { edificio -> edificioAEditar = edificio },
                                        onEliminarEdificio = { edificio -> viewModel.eliminarEdificio(edificio) },
                                        onAgregarAmbiente = { edId -> edificioIdParaNuevoAmbiente = edId },
                                        onEditarAmbiente = { amb -> ambienteAEditar = amb },
                                        onEliminarAmbiente = { amb -> viewModel.eliminarAmbiente(amb) }
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        // Vista Consulta Relacional 2: AmbienteConNotas
                        if (ambientesConNotas.isEmpty()) {
                            Text(
                                text = "No hay ambientes disponibles para mostrar notas.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(ambientesConNotas, key = { it.ambiente.id }) { item ->
                                    AmbienteNotaItem(
                                        item = item,
                                        onAgregarNota = { ambId -> ambienteIdParaNuevaNota = ambId },
                                        onEditarNota = { nota -> notaAEditar = nota },
                                        onEliminarNota = { nota -> viewModel.eliminarNota(nota) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // Diálogos modales interactivos para CRUD
    // ==========================================
    // 1. Crear Edificio
    if (mostrarCrearEdificio) {
        DialogoEdificio(
            titulo = "Registrar Nuevo Edificio",
            onConfirmar = { nombre, categoria ->
                viewModel.agregarEdificio(nombre, categoria)
                mostrarCrearEdificio = false
            },
            onDescartar = { mostrarCrearEdificio = false }
        )
    }

    // 2. Editar Edificio
    edificioAEditar?.let { edificio ->
        DialogoEdificio(
            titulo = "Actualizar Edificio (ID: ${edificio.id})",
            nombreInicial = edificio.nombre,
            categoriaInicial = edificio.categoria,
            onConfirmar = { nombre, categoria ->
                viewModel.actualizarEdificio(edificio.id, nombre, categoria)
                edificioAEditar = null
            },
            onDescartar = { edificioAEditar = null }
        )
    }

    // 3. Crear Ambiente para un Edificio
    edificioIdParaNuevoAmbiente?.let { edificioId ->
        DialogoAmbiente(
            titulo = "Nuevo Ambiente en Edificio ID $edificioId",
            onConfirmar = { nombre ->
                viewModel.agregarAmbiente(edificioId, nombre)
                edificioIdParaNuevoAmbiente = null
            },
            onDescartar = { edificioIdParaNuevoAmbiente = null }
        )
    }

    // 4. Editar Ambiente
    ambienteAEditar?.let { ambiente ->
        DialogoAmbiente(
            titulo = "Actualizar Ambiente (ID: ${ambiente.id})",
            nombreInicial = ambiente.nombre,
            onConfirmar = { nuevoNombre ->
                viewModel.actualizarAmbiente(ambiente.id, ambiente.edificioId, nuevoNombre)
                ambienteAEditar = null
            },
            onDescartar = { ambienteAEditar = null }
        )
    }

    // 5. Crear Nota para un Ambiente
    ambienteIdParaNuevaNota?.let { ambienteId ->
        DialogoNota(
            titulo = "Nueva Nota para Ambiente ID $ambienteId",
            onConfirmar = { texto ->
                viewModel.agregarNota(ambienteId, texto)
                ambienteIdParaNuevaNota = null
            },
            onDescartar = { ambienteIdParaNuevaNota = null }
        )
    }

    // 6. Editar Nota
    notaAEditar?.let { nota ->
        DialogoNota(
            titulo = "Actualizar Nota (ID: ${nota.id})",
            textoInicial = nota.texto,
            onConfirmar = { nuevoTexto ->
                viewModel.actualizarNota(nota.id, nota.ambienteId, nuevoTexto)
                notaAEditar = null
            },
            onDescartar = { notaAEditar = null }
        )
    }
}
