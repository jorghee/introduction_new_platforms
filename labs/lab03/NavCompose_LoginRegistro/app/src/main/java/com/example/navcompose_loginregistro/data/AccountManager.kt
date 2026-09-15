package com.example.navcompose_loginregistro.data

import android.content.Context
import java.io.FileNotFoundException

/**
 * Gestor de persistencia de cuentas de usuario en almacenamiento interno de Android.
 * Utiliza el archivo plano 'cuentas.txt' mediante openFileOutput y openFileInput.
 */
object AccountManager {
    private const val FILE_NAME = "cuentas.txt"
    private const val SEPARATOR = ":"

    /**
     * Registra una nueva cuenta agregando una línea en 'cuentas.txt' (MODE_APPEND).
     * En Java esto equivale a un FileWriter con append = true.
     */
    fun guardarCuenta(context: Context, usuario: String, contrasenia: String): Boolean {
        return try {
            val linea = "$usuario$SEPARATOR$contrasenia\n"
            context.openFileOutput(FILE_NAME, Context.MODE_APPEND).use { output ->
                output.write(linea.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Valida si existe una coincidencia de usuario y contraseña en 'cuentas.txt'.
     * También incluye la cuenta por defecto ("admin", "1234") especificada en el ejercicio resuelto.
     */
    fun validarCuenta(context: Context, usuario: String, contrasenia: String): Boolean {
        val cuentas = obtenerCuentasRegistradas(context)
        return cuentas.any { it.first == usuario && it.second == contrasenia }
    }

    /**
     * Lee todas las cuentas del archivo 'cuentas.txt'.
     * Retorna una lista de Pares (Pair<String, String>) de usuario y contraseña.
     */
    fun obtenerCuentasRegistradas(context: Context): List<Pair<String, String>> {
        val lista = mutableListOf<Pair<String, String>>()
        try {
            context.openFileInput(FILE_NAME).bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val partes = line.split(SEPARATOR)
                    if (partes.size >= 2) {
                        lista.add(Pair(partes[0].trim(), partes[1].trim()))
                    }
                }
            }
        } catch (_: FileNotFoundException) {
            // El archivo aún no existe porque no se ha registrado ninguna cuenta adicional
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lista
    }
}
