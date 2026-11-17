package com.example.audioguiaforeground_compose.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BannerCicloVida(
    enSegundoPlano: Boolean,
    reproduciendo: Boolean,
    modifier: Modifier = Modifier
) {
    val contenedorColor = if (enSegundoPlano) {
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    }

    val contenidoColor = if (enSegundoPlano) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    val icono = if (enSegundoPlano) Icons.Default.NotificationsActive else Icons.Default.Visibility
    val mensaje = when {
        enSegundoPlano && reproduciendo -> "Reproducción activa en segundo plano"
        enSegundoPlano -> "Sesión en segundo plano"
        reproduciendo -> "Reproduciendo narración de voz"
        else -> "Selecciona una ubicación para comenzar el recorrido"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = contenedorColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = contenidoColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = contenidoColor,
                fontSize = 12.sp
            )
        }
    }
}
