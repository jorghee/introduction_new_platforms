# UNIVERSIDAD NACIONAL DE SAN AGUSTÍN
## FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS
### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

**Formato:** Guía de Práctica de Laboratorio / Talleres / Centros de Simulación  
**Aprobación:** 2022/03/01 | **Código:** GUIA-PRLD-001

---

# GUÍA DE LABORATORIO

## INFORMACIÓN BÁSICA

| Parámetro | Detalle |
| :--- | :--- |
| **ASIGNATURA:** | INTRODUCCIÓN AL DESARROLLO DE NUEVAS PLATAFORMAS (E) |
| **TÍTULO DE LA PRÁCTICA:** | Reproductor de audio-guía con Foreground Service en Jetpack Compose |
| **NÚMERO DE PRÁCTICA:** | 11 |
| **AÑO LECTIVO:** | 2026B |
| **NRO. SEMESTRE:** | VIII |
| **TIPO DE PRÁCTICA:** | Grupal (X) |
| **MÁXIMO DE ESTUDIANTES:** | 2 |
| **FECHA INICIO:** | 17/11/2026 |
| **FECHA FIN:** | 21/11/2026 |
| **DURACIÓN:** | 100 minutos |
| **RECURSOS A UTILIZAR:** | Android Studio (Quail o superior), Kotlin, Jetpack Compose, emulador Android o dispositivo físico (API 34 o superior) |
| **DOCENTE(S):** | Mg. Roxana Evelyn Limache Calatayud |

---

## OBJETIVOS/TEMAS Y COMPETENCIAS

### OBJETIVOS:
- Comprender qué es un Foreground Service y por qué el sistema exige mostrar una notificación mientras se ejecuta.
- Implementar un reproductor de audio-guía que se ejecute como Foreground Service (iniciar, pausar, reanudar y detener).
- Controlar la visibilidad de la notificación según si la aplicación está en primer o en segundo plano.

### TEMAS:
- Foreground Service: requisitos y diferencias con un Service en segundo plano (Laboratorio 10)
- `NotificationChannel` y `startForeground()`: notificación obligatoria de un Foreground Service
- `ProcessLifecycleOwner`: detectar si la app está visible para mostrar u ocultar la notificación

### COMPETENCIAS:
- **C.a** Aplica de forma flexible técnicas, métodos, principios, normas, estándares y herramientas de ingeniería necesarias para la construcción de software e implementación de sistemas de información.
- **C.b** Investiga nuevos modelos, metodologías, técnicas, herramientas y tecnologías por ser necesarias para mantener la vigencia en el desempeño profesional.
- **C.c**
- **C.d**

---

## CONTENIDO DE LA GUÍA

### I. MARCO CONCEPTUAL

Revisar el siguiente recurso antes de la sesión:
- **Video:** *"ANDROID - FOREGROUND SERVICE ~ WITH NOTIFICATION AND BACKGROUND TASK || KOTLIN"*  
  https://www.youtube.com/watch?v=-45jGLtbhPQ
- **Android Developers, servicios en primer plano:**  
  https://developer.android.com/develop/background-work/services/foreground-services

#### ¿Qué es un Foreground Service?
Un Foreground Service es un Service que realiza una tarea que el usuario nota activamente, como reproducir audio, registrar una ruta GPS o descargar un archivo grande. A diferencia de un Service en segundo plano como el del Laboratorio 10, el sistema operativo exige que un Foreground Service muestre una notificación persistente mientras se ejecuta, informando al usuario que la app sigue trabajando aunque no esté visible en pantalla.

#### Started Service (Laboratorio 10) vs. Foreground Service
La diferencia clave no está en cómo se inician (ambos con un `Intent`), sino en las garantías y obligaciones que impone el sistema. Un started Service común puede ser detenido por Android si necesita liberar memoria, y no requiere que el usuario sepa que existe. Un Foreground Service, en cambio, obtiene una prioridad similar a la de una app visible y a cambio debe llamar a `startForeground(id, notificacion)` casi inmediatamente después de iniciarse, o el sistema lanzará una excepción. La Figura 1 compara ambos enfoques.

```
+-----------------------------------+        +-----------------------------------+
|    Started Service (Lab. 10)      |        |        Foreground Service         |
+-----------------------------------+        +-----------------------------------+
|     Sin notificación obligatoria  |        |  Notificación obligatoria y       |
|                                   |        |  visible                          |
|  El sistema puede detenerlo si    |        |  startForeground(id,              |
|  necesita liberar memoria         |        |  notificacion) es requisito       |
|                                   |        |  del sistema                      |
+-----------------------------------+        +-----------------------------------+
                  |                                            |
                  v                                            v
+-----------------------------------+        +-----------------------------------+
|      Tarea corta y discreta       |        |      Prioridad similar a una      |
|                                   |        |            app visible            |
|  El usuario no necesita saber que |        |  Ideal para tareas largas que     |
|  se está ejecutando               |        |  el usuario percibe               |
|                                   |        |  (reproducir audio)               |
+-----------------------------------+        +-----------------------------------+
```
*Figura 1. Comparación entre un Started Service (Laboratorio 10) y un Foreground Service (elaboración propia).*

#### Controlar la visibilidad de la notificación según el ciclo de vida de la app
Para que la notificación del reproductor de audio-guía solo aparezca cuando realmente aporta valor, conviene mostrarla cuando la aplicación pasa a segundo plano y ocultarla (o simplemente no publicarla) cuando la app vuelve a primer plano, ya que en ese caso el propio reproductor en pantalla ya informa el estado. La librería `androidx.lifecycle:lifecycle-process` expone un `ProcessLifecycleOwner` que notifica `onStart()`/`onStop()` para toda la aplicación, no solo para una Activity, lo que permite implementar este comportamiento de forma centralizada. La Figura 2 resume el flujo completo, desde que el usuario pulsa reproducir hasta que la notificación se oculta al volver a la app.

```
+-------------------------------------------------------+
|              Usuario pulsa Reproducir:                |
|             startForegroundService(...)               |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|                  AudioGuiaService:                    |
|       startForeground(id, notificacion) +             |
|                reproduce el audio                     |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|              App pasa a segundo plano:                |
|           ProcessLifecycleOwner.onStop()              |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|         La notificación permanece visible             |
|            con los controles de reproducción          |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|              App vuelve a primer plano:               |
|           onStart() oculta la notificación            |
+-------------------------------------------------------+
```
*Figura 2. Ciclo de visibilidad de la notificación de un Foreground Service según el estado de la app (elaboración propia).*

---

### II. EJERCICIO/PROBLEMA RESUELTO POR EL DOCENTE

A continuación se detallan, paso a paso, las instrucciones para implementar un reproductor de audio-guía (narración sobre un edificio del campus) que se ejecuta como Foreground Service. También puede apoyarse en el siguiente video tutorial:

**Video de referencia:** *"ANDROID - FOREGROUND SERVICE ~ WITH NOTIFICATION AND BACKGROUND TASK || KOTLIN"*  
https://www.youtube.com/watch?v=-45jGLtbhPQ

#### Paso 1. Crear un nuevo proyecto
- **Nombre sugerido para el proyecto:** `AudioGuiaForeground_Compose`
- **Package name:** `com.example.audioguiaforeground_compose`
- **Language:** Kotlin
- **Minimum SDK:** El que venga por defecto en la plantilla (recuerden usar un emulador o dispositivo con API 34 o superior, según la sección de Recursos a utilizar).

#### Paso 2. Modificar AndroidManifest.xml y build.gradle.kts
Abrir el archivo `AndroidManifest.xml`: agregar los permisos `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK` (obligatorio desde Android 14 para servicios de tipo mediaPlayback) y `POST_NOTIFICATIONS` (obligatorio desde Android 13), y declarar el nuevo Service dentro de la etiqueta `<application>` con `android:foregroundServiceType="mediaPlayback"`:

**Código (`AndroidManifest.xml` – fragmento):**
```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<application
    ... >
    <service
        android:name=".AudioGuiaService"
        android:exported="false"
        android:foregroundServiceType="mediaPlayback" />
    ...
</application>
```

Luego, abrir el archivo `build.gradle.kts` (Module `:app`) y agregar, dentro de dependencies, la librería `lifecycle-process` (necesaria para usar `ProcessLifecycleOwner` en el Paso 10). Después presionar **Sync Now**:

**Código (`build.gradle.kts` (Module `:app`) – fragmento):**
```kotlin
dependencies {
    ...
    implementation("androidx.lifecycle:lifecycle-process:2.9.0")
}
```

#### Paso 3. Agregar importaciones
Agregar, debajo del package y antes de la clase `MainActivity`, todas las importaciones que se usarán en este ejercicio:

**Código (importaciones):**
```kotlin
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.audioguiaforeground_compose.ui.theme.AudioGuiaForeground_ComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
```

#### Paso 4. Crear la clase AudioGuiaService
Crear la clase `AudioGuiaService`, heredando de `Service`, con un `companion object` que defina las acciones del Intent (reproducir, pausar, detener) y los identificadores del canal y de la notificación. Sobrescribir `onBind()` devolviendo `null`. Por ahora dejamos pendiente la creación del canal de notificaciones:

**Código (`AudioGuiaService` – Paso 4):**
```kotlin
class AudioGuiaService : Service() {
    private var job: Job? = null

    companion object {
        const val ACCION_REPRODUCIR = "com.example.audioguiaforeground_compose.REPRODUCIR"
        const val ACCION_PAUSAR = "com.example.audioguiaforeground_compose.PAUSAR"
        const val ACCION_DETENER = "com.example.audioguiaforeground_compose.DETENER"
        private const val ID_CANAL = "canal_audioguia"
        private const val ID_NOTIFICACION = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // onCreate se agrega en el Paso 5
}
```

#### Paso 5. Sobrescribir onCreate()
Crear el `NotificationChannel` requerido desde Android 8 (API 26) en adelante:

**Código (`AudioGuiaService` – Paso 5):**
```kotlin
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL, "Audio-guía", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
    // construirNotificacion se agrega en el Paso 6
```

#### Paso 6. Definir la función construirNotificacion()
Arma la notificación persistente que exige un Foreground Service:

**Código (`AudioGuiaService` – Paso 6):**
```kotlin
    private fun construirNotificacion(titulo: String): Notification {
        return NotificationCompat.Builder(this, ID_CANAL)
            .setContentTitle(titulo)
            .setContentText("Reproduciendo audio-guía")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()
    }
    // onStartCommand se agrega en el Paso 7
```

#### Paso 7. Sobrescribir onStartCommand()
Leyendo la acción del `Intent` recibido: al reproducir, llamar a `startForeground()` con la notificación; al pausar, cancelar el avance; al detener, cancelar el avance y finalizar el Service:

**Código (`AudioGuiaService` – Paso 7):**
```kotlin
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACCION_REPRODUCIR -> {
                startForeground(ID_NOTIFICACION, construirNotificacion("Biblioteca Central"))
                iniciarAvance()
            }
            ACCION_PAUSAR -> job?.cancel()
            ACCION_DETENER -> {
                job?.cancel()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }
    // iniciarAvance se agrega en el Paso 8
```

#### Paso 8. Definir la función iniciarAvance() y AudioGuiaService completo
Simula con una coroutine el avance de la reproducción (en un caso real, aquí se controlaría un `MediaPlayer`). Con esto, `AudioGuiaService` queda completo:

**Código (`AudioGuiaService` completo):**
```kotlin
class AudioGuiaService : Service() {
    private var job: Job? = null

    companion object {
        const val ACCION_REPRODUCIR = "com.example.audioguiaforeground_compose.REPRODUCIR"
        const val ACCION_PAUSAR = "com.example.audioguiaforeground_compose.PAUSAR"
        const val ACCION_DETENER = "com.example.audioguiaforeground_compose.DETENER"
        private const val ID_CANAL = "canal_audioguia"
        private const val ID_NOTIFICACION = 1
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL, "Audio-guía", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACCION_REPRODUCIR -> {
                startForeground(ID_NOTIFICACION, construirNotificacion("Biblioteca Central"))
                iniciarAvance()
            }
            ACCION_PAUSAR -> job?.cancel()
            ACCION_DETENER -> {
                job?.cancel()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun iniciarAvance() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            var segundos = 0
            while (true) {
                delay(1000)
                segundos++
            }
        }
    }

    private fun construirNotificacion(titulo: String): Notification {
        return NotificationCompat.Builder(this, ID_CANAL)
            .setContentTitle(titulo)
            .setContentText("Reproduciendo audio-guía")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
```

#### Paso 9. Crear el Composable PantallaAudioGuia
Obtener el contexto actual y dos variables de estado: si se está reproduciendo y si la app está en segundo plano. Por ahora dejamos el resto del cuerpo vacío:

**Código (`PantallaAudioGuia` – Paso 9):**
```kotlin
@Composable
fun PantallaAudioGuia() {
    val context = LocalContext.current
    var reproduciendo by remember { mutableStateOf(false) }
    var enSegundoPlano by remember { mutableStateOf(false) }
    // el observador de ciclo de vida se agrega en el Paso 10
}
```

#### Paso 10. Implementar el observador de ciclo de vida
Dentro de un `DisposableEffect(Unit)`, registrar un observador sobre `ProcessLifecycleOwner` que, cuando la app pasa a segundo plano (`ON_STOP`), marque `enSegundoPlano` en `true`, y cuando vuelve a primer plano (`ON_START`), lo marque en `false`, siguiendo el flujo de la Figura 2:

**Código (`PantallaAudioGuia` – Paso 10):**
```kotlin
@Composable
fun PantallaAudioGuia() {
    val context = LocalContext.current
    var reproduciendo by remember { mutableStateOf(false) }
    var enSegundoPlano by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> enSegundoPlano = true
                Lifecycle.Event.ON_START -> enSegundoPlano = false
                else -> {}
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        onDispose {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }
    // los botones se agregan en el Paso 11
}
```

#### Paso 11. Agregar controles de interfaz y código completo de PantallaAudioGuia
Agregar los botones Reproducir, Pausar y Detener, cada uno llamando a `context.startService()` con la acción correspondiente, y un texto que refleje el estado actual. Con esto, `PantallaAudioGuia` queda completo. Se agrega `statusBarsPadding()` al `Column` para que el texto y los botones no queden debajo de la barra de estado ni de la cámara del dispositivo:

**Código (`PantallaAudioGuia` completo):**
```kotlin
@Composable
fun PantallaAudioGuia() {
    val context = LocalContext.current
    var reproduciendo by remember { mutableStateOf(false) }
    var enSegundoPlano by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> enSegundoPlano = true
                Lifecycle.Event.ON_START -> enSegundoPlano = false
                else -> {}
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        onDispose {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        Text(if (reproduciendo) "Reproduciendo audio-guía..." else "Audio-guía detenida")
        if (enSegundoPlano && reproduciendo) {
            Text("La app está en segundo plano: revisa la notificación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                reproduciendo = true
                context.startService(
                    Intent(context, AudioGuiaService::class.java)
                        .setAction(AudioGuiaService.ACCION_REPRODUCIR)
                )
            }) {
                Text("Reproducir")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                reproduciendo = false
                context.startService(
                    Intent(context, AudioGuiaService::class.java)
                        .setAction(AudioGuiaService.ACCION_PAUSAR)
                )
            }) {
                Text("Pausar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                reproduciendo = false
                context.startService(
                    Intent(context, AudioGuiaService::class.java)
                        .setAction(AudioGuiaService.ACCION_DETENER)
                )
            }) {
                Text("Detener")
            }
        }
    }
}
```

#### Paso 12. Configurar MainActivity
Dentro de `MainActivity`, en el bloque `setContent`, mostrar `PantallaAudioGuia`:

**Código (`MainActivity.kt` completo):**
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudioGuiaForeground_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaAudioGuia()
                }
            }
        }
    }
}
```

#### Paso 13. Comprobación y ejecución
Ejecutar la aplicación en un emulador, presionar "Reproducir" y, sin cerrar la app, presionar el botón Home para pasarla a segundo plano; verificar que aparece la notificación persistente con el título del edificio. Si la notificación no aparece, activar los permisos de notificaciones de la aplicación en el emulador: **Ajustes (Settings) → Apps → AudioGuiaForeground_Compose → Notificaciones**, y activar todas las notificaciones de la app; luego volver a presionar "Reproducir".

---

### III. EJERCICIOS/PROBLEMAS PROPUESTOS

Mejora el reproductor de audio-guía de esta práctica:

1. Agrega un botón "Siguiente edificio" que cambie al siguiente edificio de la lista (los del Laboratorio 08) y muestre su nombre en el título de la notificación.
2. Agrega los botones "Pausar" y "Detener" dentro de la notificación, para controlar la reproducción sin abrir la app (pista: usa `addAction` en el `NotificationCompat.Builder`).
3. Usando Room (Laboratorio 09), guarda cuál fue el último edificio reproducido. Cuando se abra la app de nuevo, muestra un botón "Continuar" que retome ese edificio.
4. Haz que la audio-guía suene de verdad: agrega un archivo de audio corto (`.mp3`, por ejemplo una narración de 30 segundos sobre un edificio) en la carpeta `res/raw` y reprodúcelo con `MediaPlayer` dentro de `AudioGuiaService`. "Reproducir" inicia el audio, "Pausar" lo pausa y "Detener" lo detiene. Con esto reemplazas el avance simulado (la coroutine) por un audio real. (Pista: usa `MediaPlayer.create(this, R.raw.nombre_del_audio)`).

**Entregable:** Informe con capturas de la notificación en segundo plano, del cambio de edificio, de los botones dentro de la notificación y del archivo de audio dentro de la carpeta `res/raw`. Incluir en el informe (PDF) el enlace a un video corto (de unos 15 segundos) de la pantalla del emulador donde se escuche el audio. Verificar que el enlace tenga permisos de acceso para que se pueda abrir.

---

### IV. CUESTIONARIO

1. ¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (reproductor de audio-guía en Foreground Service, con notificación visible al pasar la app a segundo plano)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.
2. ¿Por qué es obligatorio mostrar una notificación en un Foreground Service, a diferencia de un started Service como el del Laboratorio 10? Explica qué problema busca evitar esta restricción del sistema operativo.
3. En forma individual, comenta en el video de referencia (https://www.youtube.com/watch?v=-45jGLtbhPQ) qué diferencia encuentras entre la forma de mostrar la notificación en el video y el control de visibilidad basado en `ProcessLifecycleOwner` implementado en este laboratorio. (Colocar la captura del comentario; uno por integrante del grupo).

---

### V. REFERENCIAS Y BIBLIOGRAFÍA RECOMENDADAS

[1] Android Developers, "Servicios en primer plano". [En línea]. Disponible:  
https://developer.android.com/develop/background-work/services/foreground-services

[2] "ANDROID - FOREGROUND SERVICE ~ WITH NOTIFICATION AND BACKGROUND TASK || KOTLIN", video. [En línea]. Disponible:  
https://www.youtube.com/watch?v=-45jGLtbhPQ

---

## TÉCNICAS E INSTRUMENTOS DE EVALUACIÓN

| Parámetro | Detalle |
| :--- | :--- |
| **TÉCNICAS:** | Ejercicios propuestos |
| **INSTRUMENTOS:** | Rúbrica de evaluación |

### CRITERIOS DE EVALUACIÓN
- Cumplimiento de los objetivos y ejercicios propuestos en la guía.
- Correcta aplicación de los conceptos y herramientas del tema desarrollado.
- Funcionamiento correcto de lo implementado (compilación/ejecución sin errores).
- Informe (formato estudiante) completo, con evidencias y cuestionario resuelto.
- Entrega dentro del plazo establecido.
- Se permite el uso de Inteligencia Artificial (IA) como fuente de consulta o apoyo para resolver los ejercicios; sin embargo, el desarrollo completo del laboratorio no puede basarse únicamente en respuestas generadas por IA. El estudiante debe evidenciar comprensión propia mediante la justificación razonada de sus respuestas, especialmente en el cuestionario.
