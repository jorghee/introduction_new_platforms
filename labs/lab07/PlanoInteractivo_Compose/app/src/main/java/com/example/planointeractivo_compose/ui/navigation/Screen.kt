package com.example.planointeractivo_compose.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Rutas tipadas de navegación para la aplicación de guía y planos de campus.
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Mapa : Screen(
        route = "campus",
        title = "Campus",
        icon = Icons.Default.Map
    )

    data object Plano : Screen(
        route = "plano",
        title = "Plano",
        icon = Icons.Default.Layers
    )

    data object Docente : Screen(
        route = "modulo_norte",
        title = "Módulo Norte",
        icon = Icons.Default.CorporateFare
    )

    data object Info : Screen(
        route = "servicios",
        title = "Servicios",
        icon = Icons.Default.Info
    )
}
