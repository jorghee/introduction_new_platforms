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
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planointeractivo_compose.model.Marcador

/**
 * Vista del Módulo Norte del pabellón correspondiente al Nivel 2.
 * Implementa la estructura base de dos salas con muro divisorio y marcadores interactivos
 * con una identidad visual refinada en tonos vino.
 */
@Composable
fun DocentePlanoScreen(
    modifier: Modifier = Modifier
) {
    val marcadores = remember {
        listOf(
            Marcador(Offset(95f, 145f), 32f, "Sala A - Cómputo Avanzado", "Capacidad: 25 estaciones • Equipos de alto rendimiento para IA y desarrollo móvil."),
            Marcador(Offset(255f, 145f), 32f, "Sala B - Redes y Servidores", "Capacidad: 25 estaciones • Servidores de pruebas y switches de telecomunicaciones.")
        )
    }
    var marcadorSeleccionado by remember { mutableStateOf<Marcador?>(null) }
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera institucional del módulo
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Módulo Norte - Salas de Cómputo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Nivel 2: Laboratorios de computación especializada y estaciones de trabajo académico.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Superficie de dibujo Canvas en tonos vino y neutros
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            marcadores.forEach { marcador ->
                                if ((offset - marcador.offset).getDistance() <= marcador.radio * 1.3f) {
                                    marcadorSeleccionado = marcador
                                }
                            }
                        }
                    }
            ) {
                val salaAColor = Color(0xFFF9ECEE)
                val salaBColor = Color(0xFFF4DFE2)
                val bordeColor = Color(0xFF8B2635)
                val muroColor = Color(0xFF4A101A)
                val vinoPrimary = Color(0xFF721422)

                // Salón A
                drawRect(
                    color = salaAColor,
                    topLeft = Offset(20f, 20f),
                    size = Size(150f, 250f)
                )
                drawRect(
                    color = bordeColor,
                    topLeft = Offset(20f, 20f),
                    size = Size(150f, 250f),
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Salón B
                drawRect(
                    color = salaBColor,
                    topLeft = Offset(180f, 20f),
                    size = Size(150f, 250f)
                )
                drawRect(
                    color = bordeColor,
                    topLeft = Offset(180f, 20f),
                    size = Size(150f, 250f),
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Pared divisoria reforzada
                drawLine(
                    color = muroColor,
                    start = Offset(175f, 20f),
                    end = Offset(175f, 270f),
                    strokeWidth = 5f
                )

                // Etiquetas de salones
                val textSalaA = textMeasurer.measure(
                    text = "SALA A",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1E22))
                )
                drawText(textSalaA, topLeft = Offset(65f, 32f))

                val textSalaB = textMeasurer.measure(
                    text = "SALA B",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1E22))
                )
                drawText(textSalaB, topLeft = Offset(225f, 32f))

                // Marcadores circulares interactivos
                marcadores.forEach { marcador ->
                    // Anillo exterior de pulso táctil
                    drawCircle(
                        color = vinoPrimary.copy(alpha = 0.22f),
                        radius = marcador.radio * 1.35f,
                        center = marcador.offset
                    )
                    // Círculo principal
                    drawCircle(
                        color = vinoPrimary,
                        radius = marcador.radio,
                        center = marcador.offset
                    )
                    // Núcleo blanco
                    drawCircle(
                        color = Color.White,
                        radius = marcador.radio * 0.35f,
                        center = marcador.offset
                    )
                }
            }
        }

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
                text = "Toca cualquier marcador circular para consultar la ficha técnica y equipamiento de la sala.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Diálogo emergente interactivo
        marcadorSeleccionado?.let { marcador ->
            AlertDialog(
                onDismissRequest = { marcadorSeleccionado = null },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
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
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Módulo Norte • Nivel 2",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = marcador.descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { marcadorSeleccionado = null }) {
                        Text("Entendido")
                    }
                }
            )
        }
    }
}
