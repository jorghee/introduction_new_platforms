package com.example.planointeractivo_compose.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.planointeractivo_compose.model.Marcador
import com.example.planointeractivo_compose.viewmodel.ModoVisualizacion
import com.example.planointeractivo_compose.viewmodel.PlanoUiState
import com.example.planointeractivo_compose.viewmodel.PlanoViewModel

/**
 * Pantalla principal del Plano Interactivo correspondiente a los Ejercicios Propuestos 1, 2 y 3.
 *
 * Características principales:
 * 1. Dibuja al menos 4 ambientes con Canvas y etiquetas visibles mediante [drawText].
 * 2. Carga datos dinámicos desde archivo JSON en assets a través de [PlanoRepository].
 * 3. Gestiona el ciclo de vida y los eventos mediante [PlanoViewModel] y StateFlow.
 * 4. Detección de toques sobre marcadores circulares y recintos rectangulares.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanoInteractivoScreen(
    viewModel: PlanoViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera informativa y selectores de modo
        CabeceraPlano(
            uiState = uiState,
            onModoCambiado = { modo -> viewModel.alternarModo(modo) },
            onRecargar = { viewModel.cargarPlanoEpis() }
        )

        // Contenedor del Canvas
        if (uiState.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.plano != null) {
            PlanoCanvasCard(
                uiState = uiState,
                onCanvasTapped = { offset -> viewModel.procesarToqueEnCanvas(offset) }
            )
        } else if (uiState.mensajeError != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.mensajeError ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Panel de información contextual del ambiente o marcador seleccionado
        PanelInformacionAmbiente(
            uiState = uiState,
            onLimpiarSeleccion = { viewModel.limpiarSeleccion() }
        )

        // Diálogo emergente cuando se pulsa un marcador circular
        uiState.marcadorSeleccionado?.let { marcador ->
            MarcadorDialog(
                marcador = marcador,
                onDismiss = { viewModel.cerrarDialogoMarcador() }
            )
        }
    }
}

@Composable
private fun CabeceraPlano(
    uiState: PlanoUiState,
    onModoCambiado: (ModoVisualizacion) -> Unit,
    onRecargar: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = uiState.plano?.edificio ?: "Plano del Pabellón",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = uiState.plano?.descripcion
                    ?: "Visualización vectorial con Canvas en Jetpack Compose",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de niveles del pabellón
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = uiState.modo == ModoVisualizacion.PABELLON_EPIS,
                    onClick = { onModoCambiado(ModoVisualizacion.PABELLON_EPIS) },
                    label = { Text("Nivel 1 (Principal)") },
                    leadingIcon = {
                        if (uiState.modo == ModoVisualizacion.PABELLON_EPIS) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )

                FilterChip(
                    selected = uiState.modo == ModoVisualizacion.DOCENTE_MUESTRA,
                    onClick = { onModoCambiado(ModoVisualizacion.DOCENTE_MUESTRA) },
                    label = { Text("Nivel 2 (Módulo Norte)") }
                )

                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = onRecargar,
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar vista",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta contenedora que aloja la superficie de dibujo [Canvas].
 * Dibuja los rectángulos de cada ambiente, sus bordes, etiquetas de texto, muros y marcadores.
 */
@Composable
private fun PlanoCanvasCard(
    uiState: PlanoUiState,
    onCanvasTapped: (Offset) -> Unit
) {
    val plano = uiState.plano ?: return
    val textMeasurer = rememberTextMeasurer()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .pointerInput(plano) {
                        detectTapGestures { offset ->
                            onCanvasTapped(offset)
                        }
                    }
                    .semantics {
                        contentDescription = "Plano interactivo del pabellón con salones y marcadores táctiles"
                    }
            ) {
                // 1. Dibujar ambientes (Rectángulos con color y bordes)
                plano.ambientes.forEach { ambiente ->
                    val colorRelleno = parseColor(ambiente.colorHex, Color(0xFFF9ECEE))
                    val colorBorde = parseColor(ambiente.bordeHex, Color(0xFF8B2635))
                    val estaSeleccionado = (ambiente.id == uiState.ambienteSeleccionado?.id)

                    // Relleno del ambiente
                    drawRect(
                        color = if (estaSeleccionado) colorRelleno.copy(alpha = 0.95f) else colorRelleno,
                        topLeft = ambiente.topLeft,
                        size = ambiente.size
                    )

                    // Borde perimetral del ambiente con acento vino en selección
                    drawRect(
                        color = if (estaSeleccionado) Color(0xFF721422) else colorBorde,
                        topLeft = ambiente.topLeft,
                        size = ambiente.size,
                        style = Stroke(width = if (estaSeleccionado) 3.5.dp.toPx() else 1.5.dp.toPx())
                    )

                    // 2. Dibujar etiqueta visible de texto dentro del ambiente
                    val textoEtiqueta = ambiente.etiqueta
                    val textLayout = textMeasurer.measure(
                        text = textoEtiqueta,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E1E22),
                            textAlign = TextAlign.Center
                        )
                    )

                    // Centrar etiqueta en el tercio superior del ambiente
                    val labelX = ambiente.topLeft.x + (ambiente.size.width - textLayout.size.width) / 2f
                    val labelY = ambiente.topLeft.y + 12f

                    drawText(
                        textLayoutResult = textLayout,
                        topLeft = Offset(labelX.coerceAtLeast(ambiente.topLeft.x + 4f), labelY)
                    )
                }

                // 3. Dibujar líneas y muros divisorios
                plano.lineas.forEach { linea ->
                    val colorLinea = parseColor(linea.colorHex, Color(0xFF2E1E22))
                    drawLine(
                        color = colorLinea,
                        start = linea.start,
                        end = linea.end,
                        strokeWidth = linea.grosor
                    )
                }

                // 4. Dibujar marcadores circulares interactivos
                plano.marcadores.forEach { marcador ->
                    val colorMarcador = parseColor(marcador.colorHex, Color(0xFF721422))
                    val esMarcadorActivo = (marcador.id == uiState.marcadorSeleccionado?.id)

                    // Anillo exterior de contraste/toque
                    drawCircle(
                        color = colorMarcador.copy(alpha = if (esMarcadorActivo) 0.40f else 0.20f),
                        radius = marcador.radio * 1.35f,
                        center = marcador.offset
                    )

                    // Círculo principal del marcador
                    drawCircle(
                        color = colorMarcador,
                        radius = marcador.radio,
                        center = marcador.offset
                    )

                    // Núcleo central blanco indicativo de pin
                    drawCircle(
                        color = Color.White,
                        radius = marcador.radio * 0.38f,
                        center = marcador.offset
                    )
                }
            }
        }
    }
}

/**
 * Panel informativo que complementa la interacción con el plano.
 */
@Composable
private fun PanelInformacionAmbiente(
    uiState: PlanoUiState,
    onLimpiarSeleccion: () -> Unit
) {
    val ambiente = uiState.ambienteSeleccionado

    if (ambiente != null) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ambiente seleccionado: ${ambiente.nombre}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    TextButton(onClick = onLimpiarSeleccion) {
                        Text("Deseleccionar", fontSize = 12.sp)
                    }
                }
                Text(
                    text = "Espacio académico y formativo • Acceso disponible según programación horaria",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Selecciona una sala o un marcador en el plano para consultar su información.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * Diálogo modal [AlertDialog] con la información del marcador seleccionado.
 */
@Composable
private fun MarcadorDialog(
    marcador: Marcador,
    onDismiss: () -> Unit
) {
    val colorMarcador = parseColor(marcador.colorHex, MaterialTheme.colorScheme.primary)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(colorMarcador, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = marcador.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "Categoría: ${marcador.categoria}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = marcador.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Ubicación en Pabellón EPIS • Punto de referencia institucional",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendido")
            }
        }
    )
}

/**
 * Función auxiliar para parsear cadenas de color hexadecimal (#RRGGBB).
 */
private fun parseColor(hex: String, defaultColor: Color): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorLong = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(colorLong or 0x00000000FF000000)
        } else {
            Color(colorLong)
        }
    } catch (_: Exception) {
        defaultColor
    }
}
