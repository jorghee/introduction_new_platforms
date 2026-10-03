package com.example.navigationbar_multiple_composables.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigationbar_multiple_composables.ui.screens.EdificiosScreen
import com.example.navigationbar_multiple_composables.ui.screens.HomeScreen
import com.example.navigationbar_multiple_composables.ui.screens.MapaScreen
import com.example.navigationbar_multiple_composables.ui.screens.PerfilScreen
import com.example.navigationbar_multiple_composables.viewmodel.SeleccionViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // Instancia única del ViewModel compartida en el scope del NavGraph (MainScreen)
    val sharedViewModel: SeleccionViewModel = viewModel()

    val items = listOf(Screen.Home, Screen.Edificios, Screen.Mapa, Screen.Perfil)
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    val icon = when (screen) {
                        Screen.Home -> Icons.Default.Home
                        Screen.Edificios -> Icons.AutoMirrored.Filled.List
                        Screen.Mapa -> Icons.Default.Place
                        Screen.Perfil -> Icons.Default.Person
                    }
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = { 
                            // Evitar reconstruir el stack innecesariamente si ya estamos en la ruta
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel = sharedViewModel)
            }
            composable(Screen.Edificios.route) {
                EdificiosScreen(viewModel = sharedViewModel)
            }
            composable(Screen.Mapa.route) {
                MapaScreen()
            }
            composable(Screen.Perfil.route) {
                PerfilScreen()
            }
        }
    }
}
