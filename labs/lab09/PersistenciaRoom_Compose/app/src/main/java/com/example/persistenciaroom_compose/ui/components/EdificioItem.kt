package com.example.persistenciaroom_compose.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.persistenciaroom_compose.data.local.relation.EdificioConAmbientes

@Composable
fun EdificioItem(
    item: EdificioConAmbientes,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.edificio.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Categoría: ${item.edificio.categoria}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (item.ambientes.isEmpty()) {
                Text(
                    text = "Sin ambientes registrados",
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Ambientes:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                item.ambientes.forEach { ambiente ->
                    Text(
                        text = "• ${ambiente.nombre}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                    )
                }
            }
        }
    }
}
