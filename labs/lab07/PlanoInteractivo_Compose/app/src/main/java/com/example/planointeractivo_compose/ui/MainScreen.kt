package com.example.planointeractivo_compose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planointeractivo_compose.ui.navigation.Screen
import com.example.planointeractivo_compose.ui.screens.DocentePlanoScreen
import com.example.planointeractivo_compose.ui.screens.InfoScreen
import com.example.planointeractivo_compose.ui.screens.MapaCampusScreen
import com.example.planointeractivo_compose.ui.screens.PlanoInteractivoScreen
import com.example.planointeractivo_compose.viewmodel.PlanoViewModel

/**
 * Pantalla principal orquestadora con Scaffold, TopAppBar y NavigationBar.
 * Centraliza la navegación entre pantallas y alberga el ViewModel compartido.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    planoViewModel: PlanoViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItems = listOf(
        Screen.Mapa,
        Screen.Plano,
        Screen.Docente,
        Screen.Info
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            Screen.Mapa.route -> "Campus Universitario"
                            Screen.Plano.route -> "Plano del Pabellón"
                            Screen.Docente.route -> "Módulo Norte"
                            Screen.Info.route -> "Servicios del Pabellón"
                            else -> "Guía de Ambientes"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                navigationItems.forEach { screen ->
                    val selected = (currentRoute == screen.route)
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Mapa.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Pantalla 1: Mapa del Campus con tarjeta de acceso al plano (Ejercicio 4)
            composable(Screen.Mapa.route) {
                MapaCampusScreen(
                    onNavegarAPlano = {
                        navController.navigate(Screen.Plano.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // Pantalla 2: Plano interactivo completo (4 ambientes + JSON + ViewModel)
            composable(Screen.Plano.route) {
                PlanoInteractivoScreen(viewModel = planoViewModel)
            }

            // Pantalla 3: Muestra docente (Pasos 1 al 11)
            composable(Screen.Docente.route) {
                DocentePlanoScreen()
            }

            // Pantalla 4: Información académica
            composable(Screen.Info.route) {
                InfoScreen()
            }
        }
    }
}
