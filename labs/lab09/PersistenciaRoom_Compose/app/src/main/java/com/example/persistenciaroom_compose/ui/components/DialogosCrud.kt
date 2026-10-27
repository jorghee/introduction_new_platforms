package com.example.persistenciaroom_compose.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DialogoEdificio(
    titulo: String,
    nombreInicial: String = "",
    categoriaInicial: String = "",
    onConfirmar: (nombre: String, categoria: String) -> Unit,
    onDescartar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var categoria by remember { mutableStateOf(categoriaInicial) }

    AlertDialog(
        onDismissRequest = onDescartar,
        title = { Text(text = titulo) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Edificio") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría (ej. Académico, Servicios)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombre.isNotBlank() && categoria.isNotBlank()) {
                        onConfirmar(nombre, categoria)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDescartar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoAmbiente(
    titulo: String,
    nombreInicial: String = "",
    onConfirmar: (nombre: String) -> Unit,
    onDescartar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }

    AlertDialog(
        onDismissRequest = onDescartar,
        title = { Text(text = titulo) },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Ambiente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombre.isNotBlank()) {
                        onConfirmar(nombre)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDescartar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoNota(
    titulo: String,
    textoInicial: String = "",
    onConfirmar: (texto: String) -> Unit,
    onDescartar: () -> Unit
) {
    var texto by remember { mutableStateOf(textoInicial) }

    AlertDialog(
        onDismissRequest = onDescartar,
        title = { Text(text = titulo) },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Texto de la Nota / Observación") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (texto.isNotBlank()) {
                        onConfirmar(texto)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDescartar) {
                Text("Cancelar")
            }
        }
    )
}
