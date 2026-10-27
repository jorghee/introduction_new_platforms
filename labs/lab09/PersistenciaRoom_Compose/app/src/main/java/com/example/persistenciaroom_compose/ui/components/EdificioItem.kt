package com.example.persistenciaroom_compose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.persistenciaroom_compose.data.local.entity.AmbienteEntity
import com.example.persistenciaroom_compose.data.local.entity.EdificioEntity
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes

@Composable
fun EdificioItem(
    item: EdificioConAmbientes,
    onEditarEdificio: (EdificioEntity) -> Unit,
    onEliminarEdificio: (EdificioEntity) -> Unit,
    onAgregarAmbiente: (edificioId: Int) -> Unit,
    onEditarAmbiente: (AmbienteEntity) -> Unit,
    onEliminarAmbiente: (AmbienteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera del Edificio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.edificio.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "ID: ${item.edificio.id} | Categoría: ${item.edificio.categoria}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Botones de acción del Edificio
                Row {
                    OutlinedButton(
                        onClick = { onEditarEdificio(item.edificio) },
                        contentPadding = ButtonDefaults.TextButtonContentPadding,
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Editar", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    OutlinedButton(
                        onClick = { onEliminarEdificio(item.edificio) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        contentPadding = ButtonDefaults.TextButtonContentPadding,
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Eliminar", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))

            // Sección de Ambientes (Relación 1 a N)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ambientes asignados (${item.ambientes.size}):",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                OutlinedButton(
                    onClick = { onAgregarAmbiente(item.edificio.id) },
                    modifier = Modifier.height(30.dp),
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Text("+ Ambiente", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (item.ambientes.isEmpty()) {
                Text(
                    text = "No hay ambientes registrados en este edificio.",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                item.ambientes.forEach { ambiente ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• ${ambiente.nombre} (ID: ${ambiente.id})",
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Row {
                                OutlinedButton(
                                    onClick = { onEditarAmbiente(ambiente) },
                                    modifier = Modifier.height(28.dp),
                                    contentPadding = ButtonDefaults.TextButtonContentPadding
                                ) {
                                    Text("Editar", fontSize = 10.sp)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedButton(
                                    onClick = { onEliminarAmbiente(ambiente) },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    modifier = Modifier.height(28.dp),
                                    contentPadding = ButtonDefaults.TextButtonContentPadding
                                ) {
                                    Text("Borrar", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
