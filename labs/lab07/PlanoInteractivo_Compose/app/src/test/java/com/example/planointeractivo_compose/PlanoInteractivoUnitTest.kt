package com.example.planointeractivo_compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.planointeractivo_compose.model.Ambiente
import com.example.planointeractivo_compose.model.Marcador
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias para validar los algoritmos matemáticos y la lógica de detección
 * de toques sobre el plano en Canvas (Distancia Euclidiana y AABB).
 */
class PlanoInteractivoUnitTest {

    @Test
    fun marcadorCircular_deteccionDistanciaEuclidiana_puntoDentroRetornaTrue() {
        val centro = Offset(100f, 150f)
        val radio = 40f
        val marcador = Marcador(
            offset = centro,
            radio = radio,
            titulo = "Sala A",
            descripcion = "Sala de cómputo 1"
        )

        // Toque en el centro exacto (distancia = 0)
        val toqueCentro = Offset(100f, 150f)
        val distanciaCentro = (toqueCentro - marcador.offset).getDistance()
        assertTrue(distanciaCentro <= marcador.radio)

        // Toque a 20px de distancia (dentro del radio de 40px)
        val toqueCercano = Offset(120f, 150f)
        val distanciaCercana = (toqueCercano - marcador.offset).getDistance()
        assertEquals(20f, distanciaCercana, 0.001f)
        assertTrue(distanciaCercana <= marcador.radio)

        // Toque justo en el borde perimetral (40px)
        val toqueBorde = Offset(100f, 190f)
        val distanciaBorde = (toqueBorde - marcador.offset).getDistance()
        assertEquals(40f, distanciaBorde, 0.001f)
        assertTrue(distanciaBorde <= marcador.radio)
    }

    @Test
    fun marcadorCircular_deteccionDistanciaEuclidiana_puntoFueraRetornaFalse() {
        val marcador = Marcador(
            offset = Offset(100f, 150f),
            radio = 40f,
            titulo = "Sala A",
            descripcion = "Sala de cómputo 1"
        )

        // Toque a 50px a la derecha (fuera del radio)
        val toqueLejano = Offset(150f, 150f)
        val distancia = (toqueLejano - marcador.offset).getDistance()
        assertEquals(50f, distancia, 0.001f)
        assertFalse(distancia <= marcador.radio)
    }

    @Test
    fun ambienteRectangular_colisionAABB_validaCorrectamenteLimites() {
        val ambiente = Ambiente(
            id = "lab101",
            nombre = "Lab 101",
            etiqueta = "Lab 101",
            topLeft = Offset(20f, 30f),
            size = Size(155f, 130f)
        )

        // Punto interior: X=50, Y=60
        assertTrue(ambiente.contienePunto(Offset(50f, 60f)))

        // Punto en la esquina superior izquierda
        assertTrue(ambiente.contienePunto(Offset(20f, 30f)))

        // Punto en la esquina inferior derecha: X = 20+155 = 175, Y = 30+130 = 160
        assertTrue(ambiente.contienePunto(Offset(175f, 160f)))

        // Punto fuera por la izquierda (X=15)
        assertFalse(ambiente.contienePunto(Offset(15f, 50f)))

        // Punto fuera por la derecha (X=180)
        assertFalse(ambiente.contienePunto(Offset(180f, 50f)))

        // Punto fuera por arriba (Y=20)
        assertFalse(ambiente.contienePunto(Offset(50f, 20f)))

        // Punto fuera por abajo (Y=170)
        assertFalse(ambiente.contienePunto(Offset(50f, 170f)))
    }

    @Test
    fun resolucionToques_priorizaMarcadorCircularSobreAmbiente() {
        val ambiente = Ambiente(
            id = "lab101",
            nombre = "Lab 101",
            etiqueta = "Lab 101",
            topLeft = Offset(20f, 30f),
            size = Size(155f, 130f)
        )
        val marcador = Marcador(
            offset = Offset(97.5f, 95f),
            radio = 30f,
            titulo = "Lab 101",
            descripcion = "Detalles"
        )

        val toqueSobreMarcador = Offset(98f, 96f)

        // El toque cae simultáneamente dentro del rectángulo y dentro del círculo
        assertTrue(ambiente.contienePunto(toqueSobreMarcador))
        assertTrue((toqueSobreMarcador - marcador.offset).getDistance() <= marcador.radio)

        // La regla de negocio prioriza el marcador
        val marcadores = listOf(marcador)
        val marcadorEncontrado = marcadores.firstOrNull {
            (toqueSobreMarcador - it.offset).getDistance() <= it.radio
        }
        assertNotNull(marcadorEncontrado)
        assertEquals("Lab 101", marcadorEncontrado?.titulo)
    }
}
