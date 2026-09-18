package com.example.diego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diego.ui.theme.DiegoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiegoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TicketPurchaseScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TicketPurchaseScreen(modifier: Modifier = Modifier) {
    // Estados para guardar la selección del usuario
    var ticketCount by remember { mutableIntStateOf(2) }
    var hasCanchita by remember { mutableStateOf(false) }
    var hasBebida by remember { mutableStateOf(false) }
    var hasCoupon by remember { mutableStateOf(false) }

    // Precios
    val ticketPrice = 15.0
    val canchitaPrice = 12.0 
    val bebidaPrice = 8.0

    // Cálculos dinámicos optimizados con derivedStateOf
    val subtotal by remember {
        derivedStateOf {
            (ticketCount * ticketPrice) +
                    (if (hasCanchita) canchitaPrice else 0.0) +
                    (if (hasBebida) bebidaPrice else 0.0)
        }
    }

    val discount by remember {
        derivedStateOf {
            if (hasCoupon) subtotal * 0.10 else 0.0
        }
    }

    val total by remember {
        derivedStateOf {
            subtotal - discount
        }
    }

    // Columna principal que organiza las secciones mediante verticalArrangement
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Sección 1: Título
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "COMPRA DE ENTRADAS",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Sección 2: Cantidad de entradas
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Cantidad de entradas")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (ticketCount > 1) ticketCount-- }) { Text("-") }
                Text(
                    text = ticketCount.toString(),
                    modifier = Modifier.padding(horizontal = 24.dp),
                    fontSize = 18.sp
                )
                Button(onClick = { ticketCount++ }) { Text("+") }
            }
        }

        // Sección 3: Extras
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Extras", fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasCanchita, onCheckedChange = { hasCanchita = it })
                Text("Canchita")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasBebida, onCheckedChange = { hasBebida = it })
                Text("Bebida")
            }
        }

        // Sección 4: Cupón
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = hasCoupon, onCheckedChange = { hasCoupon = it })
                Text("Aplicar cupón")
            }
        }

        // Sección 5: Resumen de cobro
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Subtotal: S/ ${subtotal.toInt()}")
            Text(text = "Descuento: S/ ${discount.toInt()}")
            Text(text = "Total: S/ ${total.toInt()}", fontWeight = FontWeight.Bold)
        }

        // Sección 6: Botón de acción
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { /* Acción al comprar */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("COMPRAR")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketPurchaseScreenPreview() {
    DiegoTheme {
        TicketPurchaseScreen()
    }
}
