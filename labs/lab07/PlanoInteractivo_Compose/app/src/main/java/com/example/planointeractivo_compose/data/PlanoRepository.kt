package com.example.planointeractivo_compose.data

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.planointeractivo_compose.model.Ambiente
import com.example.planointeractivo_compose.model.LineaPlano
import com.example.planointeractivo_compose.model.Marcador
import com.example.planointeractivo_compose.model.PlanoEdificio
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Repositorio encargado de gestionar la carga y persistencia en memoria
 * de los datos del plano arquitectónico desde fuentes locales (archivo JSON en assets).
 *
 * Implementa el patrón Repository para aislar la capa de presentación de los detalles
 * de deserialización y lectura del sistema de archivos de Android.
 */
class PlanoRepository(private val context: Context) {

    /**
     * Carga y deserializa el archivo JSON almacenado en assets/plano_epis.json.
     * Retorna un [Result] con el objeto [PlanoEdificio] o la excepción capturada.
     */
    fun cargarPlanoDesdeAssets(fileName: String = "plano_epis.json"): Result<PlanoEdificio> {
        return runCatching {
            val jsonString = context.assets.open(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readText()
                }
            }
            parsearPlanoJson(jsonString)
        }
    }

    /**
     * Parsea la cadena JSON utilizando la biblioteca nativa [JSONObject] de Android.
     */
    private fun parsearPlanoJson(jsonString: String): PlanoEdificio {
        val root = JSONObject(jsonString)
        val edificio = root.optString("edificio", "Pabellón EPIS - UNSA")
        val piso = root.optString("piso", "Piso 1")
        val descripcion = root.optString("descripcion", "")

        // Ambientes (Rectángulos con etiquetas)
        val ambientes = mutableListOf<Ambiente>()
        val ambientesArray = root.optJSONArray("ambientes")
        if (ambientesArray != null) {
            for (i in 0 until ambientesArray.length()) {
                val obj = ambientesArray.getJSONObject(i)
                ambientes.add(
                    Ambiente(
                        id = obj.getString("id"),
                        nombre = obj.getString("nombre"),
                        etiqueta = obj.getString("etiqueta"),
                        topLeft = Offset(
                            obj.getDouble("x").toFloat(),
                            obj.getDouble("y").toFloat()
                        ),
                        size = Size(
                            obj.getDouble("ancho").toFloat(),
                            obj.getDouble("alto").toFloat()
                        ),
                        colorHex = obj.optString("colorHex", "#BBDEFB"),
                        bordeHex = obj.optString("bordeHex", "#1976D2")
                    )
                )
            }
        }

        // Líneas divisorias
        val lineas = mutableListOf<LineaPlano>()
        val lineasArray = root.optJSONArray("lineas")
        if (lineasArray != null) {
            for (i in 0 until lineasArray.length()) {
                val obj = lineasArray.getJSONObject(i)
                lineas.add(
                    LineaPlano(
                        start = Offset(
                            obj.getDouble("startX").toFloat(),
                            obj.getDouble("startY").toFloat()
                        ),
                        end = Offset(
                            obj.getDouble("endX").toFloat(),
                            obj.getDouble("endY").toFloat()
                        ),
                        colorHex = obj.optString("colorHex", "#424242"),
                        grosor = obj.optDouble("grosor", 4.0).toFloat()
                    )
                )
            }
        }

        // Marcadores interactivos
        val marcadores = mutableListOf<Marcador>()
        val marcadoresArray = root.optJSONArray("marcadores")
        if (marcadoresArray != null) {
            for (i in 0 until marcadoresArray.length()) {
                val obj = marcadoresArray.getJSONObject(i)
                marcadores.add(
                    Marcador(
                        id = obj.optString("id", "m$i"),
                        offset = Offset(
                            obj.getDouble("x").toFloat(),
                            obj.getDouble("y").toFloat()
                        ),
                        radio = obj.optDouble("radio", 35.0).toFloat(),
                        titulo = obj.getString("titulo"),
                        descripcion = obj.getString("descripcion"),
                        categoria = obj.optString("categoria", "General"),
                        colorHex = obj.optString("colorHex", "#D32F2F")
                    )
                )
            }
        }

        return PlanoEdificio(
            edificio = edificio,
            piso = piso,
            descripcion = descripcion,
            ambientes = ambientes,
            lineas = lineas,
            marcadores = marcadores
        )
    }

    /**
     * Retorna el modelo arquitectónico correspondiente al segundo nivel del pabellón,
     * permitiendo una visualización rápida entre diferentes módulos de la edificación.
     */
    fun obtenerPlanoDocenteMuestra(): PlanoEdificio {
        return PlanoEdificio(
            edificio = "Pabellón EPIS - Módulo Norte",
            piso = "Nivel 2",
            descripcion = "Salas de cómputo avanzadas y laboratorios complementarios.",
            ambientes = listOf(
                Ambiente(
                    id = "sala_comp_1",
                    nombre = "Sala A - Cómputo General",
                    etiqueta = "Sala A\n(Cómputo)",
                    topLeft = Offset(20f, 20f),
                    size = Size(150f, 250f),
                    colorHex = "#F9ECEE",
                    bordeHex = "#8B2635"
                ),
                Ambiente(
                    id = "sala_comp_2",
                    nombre = "Sala B - Cómputo Especializado",
                    etiqueta = "Sala B\n(Especializada)",
                    topLeft = Offset(180f, 20f),
                    size = Size(150f, 250f),
                    colorHex = "#F3EFEA",
                    bordeHex = "#594A42"
                )
            ),
            lineas = listOf(
                LineaPlano(
                    start = Offset(170f, 20f),
                    end = Offset(170f, 270f),
                    colorHex = "#2E1E22",
                    grosor = 4f
                )
            ),
            marcadores = listOf(
                Marcador(
                    offset = Offset(100f, 150f),
                    radio = 40f,
                    titulo = "Sala A - Cómputo General",
                    descripcion = "Capacidad: 30 estaciones con acceso libre para estudiantes de pregrado y prácticas guiadas.",
                    categoria = "Docencia",
                    colorHex = "#721422"
                ),
                Marcador(
                    offset = Offset(250f, 150f),
                    radio = 40f,
                    titulo = "Sala B - Cómputo Especializado",
                    descripcion = "Capacidad: 25 estaciones con software especializado en modelado, diseño y análisis de datos.",
                    categoria = "Investigación",
                    colorHex = "#8C1D40"
                )
            )
        )
    }
}
