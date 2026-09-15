package com.example.navcompose_loginregistro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navcompose_loginregistro.ui.screens.HomeScreen
import com.example.navcompose_loginregistro.ui.screens.LoginScreen
import com.example.navcompose_loginregistro.ui.screens.RegistroScreen
import com.example.navcompose_loginregistro.ui.theme.NavCompose_LoginRegistroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavCompose_LoginRegistroTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Ruta: Login
                        composable("login") {
                            LoginScreen(
                                onLoginExitoso = { usuario ->
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Bienvenido $usuario",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Navegación con argumento dinámico hacia HomeScreen (Ejercicio Propuesto 1)
                                    val safeUsuario = android.net.Uri.encode(usuario)
                                    navController.navigate("home/$safeUsuario") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onIrARegistro = {
                                    navController.navigate("registro")
                                }
                            )
                        }

                        // 2. Ruta: Registro
                        composable("registro") {
                            RegistroScreen(
                                onRegistroExitoso = {
                                    navController.popBackStack()
                                },
                                onCancelar = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 3. Ruta: Home con parámetro de ruta {usuario}
                        composable(
                            route = "home/{usuario}",
                            arguments = listOf(
                                navArgument("usuario") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val usuarioParam = backStackEntry.arguments?.getString("usuario") ?: "Usuario"
                            HomeScreen(
                                usuario = usuarioParam,
                                onCerrarSesion = {
                                    navController.navigate("login") {
                                        popUpTo("home/{usuario}") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
